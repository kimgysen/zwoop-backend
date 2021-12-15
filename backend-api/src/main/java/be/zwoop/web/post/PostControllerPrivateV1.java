package be.zwoop.web.post;


import be.zwoop.config.security.facade.AuthenticationFacade;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.web.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/posts")
public class PostControllerPrivateV1 {

    private AuthenticationFacade authenticationFacade;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> savePost(@Valid @RequestBody PostDto postDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        // TODO: Optional check if active and not blocked
        UserEntity askerEntity = userRepository.getById(principalId);

        Optional<PostEntity> postEntityOpt = postRepository.findByAskerAndPostTitle(askerEntity, postDto.getTitle());

        if (postEntityOpt.isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, "Post already exists with id: " + postEntityOpt.get().getPostId());

        } else {
            List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(postDto.getTagIds());
            PostEntity toSave = PostEntity.builder()
                    .postTitle(postDto.getTitle())
                    .postText(postDto.getText())
                    .bidPrice(postDto.getBidPrice())
                    .tags(tagEntities)
                    .build();
            PostEntity savedPost = postRepository.saveAndFlush(toSave);

            URI uri = UriComponentsBuilder
                    .fromPath(("/{id}"))
                    .buildAndExpand(savedPost.getPostId()).toUri();
            return created(uri).build();
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable UUID postId) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        // TODO: Optional check if active and not blocked
        UserEntity askerEntity = userRepository.getById(principalId);
        Optional<PostEntity> postEntityOpt = postRepository.findById(postId);

        if (postEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);

        } else {
            PostEntity toUpdate = postEntityOpt.get();

            toUpdate.setPostTitle(postDto.getTitle());
            toUpdate.setPostText(postDto.getText());
            List<TagEntity> tagEntities = tagRepository.findAllByTagIdIn(postDto.getTagIds());
            toUpdate.setTags(tagEntities);

            postRepository.saveAndFlush(toUpdate);

            return noContent().build();
        }
    }

}
