package be.zwoop.web.post;


import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.web.post.dto.PostDto;
import be.zwoop.service.post.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/post")
public class PostControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final PostService postService;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostDto postDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        Optional<UserEntity> askerEntityOpt = postService.findAskerByUserId(principalId);
        if (askerEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Asker doesn\'t exist for UUID: " + principalId.toString());

        UserEntity askerEntity = askerEntityOpt.get();

        Optional<PostEntity> postEntityOpt = postService.findByTitleAndAsker(postDto.getTitle(), askerEntity);
        if (postEntityOpt.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Post already exists with id: " + postEntityOpt.get().getPostId());
        }

        PostEntity savedPost = postService.createPost(postDto, askerEntity);

        URI uri = UriComponentsBuilder
                .fromPath(("/post/{id}"))
                .buildAndExpand(savedPost.getPostId()).toUri();
        return created(uri).build();

    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable UUID postId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        Optional<UserEntity> principalEntityOpt = postService.findAskerByUserId(principalId);
        if (principalEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Asker doesn\'t exist for UUID: " + principalId.toString());
        UserEntity principalEntity = principalEntityOpt.get();

        Optional<PostEntity> postEntityOpt = postService.findByPostId(postId);
        if (postEntityOpt.isEmpty())
            throw new ResponseStatusException(NOT_FOUND);

        PostEntity toUpdate = postEntityOpt.get();
        if (!toUpdate.getAsker().getUserId().equals(principalEntity.getUserId())) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (postService.hasPostChanged(toUpdate, postDto)) {
            postService.updatePost(toUpdate, postDto);
            postService.sendPostChangedToQueue(toUpdate);
        }

        return noContent().build();
    }

    // TODO: Delete post

}
