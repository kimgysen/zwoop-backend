package be.zwoop.web.post;


import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.post.db.PostDbService;
import be.zwoop.service.post.notification.PostNotificationService;
import be.zwoop.web.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static be.zwoop.domain.enum_type.PostStatusEnum.POST_INIT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/post")
public class PostControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final PostDbService postDbService;
    private final PostNotificationService postNotificationService;

    @PostMapping
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostDto postDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        Optional<UserEntity> opEntityOpt = postDbService.findByUserId(principalId);
        if (opEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Op doesn\'t exist for UUID: " + principalId.toString());

        UserEntity askerEntity = opEntityOpt.get();

        Optional<PostEntity> postEntityOpt = postDbService.findByTitleAndOp(postDto.getTitle(), askerEntity);
        if (postEntityOpt.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Post already exists with id: " + postEntityOpt.get().getPostId());
        }

        PostEntity savedPost = postDbService.createPost(postDto, askerEntity);

        URI uri = UriComponentsBuilder
                .fromPath(("/post/{id}"))
                .buildAndExpand(savedPost.getPostId()).toUri();
        return created(uri).build();

    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable UUID postId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        Optional<UserEntity> principalEntityOpt = postDbService.findByUserId(principalId);
        if (principalEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Op doesn\'t exist for UUID: " + principalId.toString());
        UserEntity principalEntity = principalEntityOpt.get();

        Optional<PostEntity> postEntityOpt = postDbService.findByPostId(postId);
        if (postEntityOpt.isEmpty())
            throw new ResponseStatusException(NOT_FOUND);

        PostEntity toUpdate = postEntityOpt.get();
        if (!toUpdate.getOp().getUserId().equals(principalEntity.getUserId())) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

        if (!Objects.equals(toUpdate.getPostState().getPostStatus().getStatus(), POST_INIT.name())) {
            throw new ResponseStatusException(CONFLICT, "Cannot update a post that is doesn\'t have INIT status.");
        }

        if (postDbService.hasPostChanged(toUpdate, postDto)) {
            postDbService.updatePost(toUpdate, postDto);
            postNotificationService.sendPostChangedNotification(toUpdate);
        }

        return noContent().build();
    }

    // TODO: Delete post

}
