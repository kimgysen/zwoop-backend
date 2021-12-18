package be.zwoop.web.post;


import be.zwoop.domain.enum_type.PostFeedTypeEnum;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.post.PostStatusEntity;
import be.zwoop.repository.post.PostStatusRepository;
import be.zwoop.web.exception.RequestParamException;
import be.zwoop.web.post.dto.ValidFeedParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/posts")
public class PostControllerPublicV1 {

    private PostRepository postRepository;
    private PostControllerValidationHelper validator;
    private PostStatusRepository postStatusRepository;


    @GetMapping("/{postId}")
    public ResponseEntity<PostEntity> getPost(@PathVariable UUID postId) {
        Optional<PostEntity> postEntity = postRepository.findById(postId);

        if (postEntity.isPresent()) {
            return ok(postEntity.get());

        } else {
            throw new ResponseStatusException(NOT_FOUND);

        }
    }

    @GetMapping
    public Page<PostEntity> getPosts(
            @RequestParam(value = "feedType") PostFeedTypeEnum feedType,
            @RequestParam(value = "tagId") Optional<Long> tagIdOpt,
            @NotNull final Pageable pageable) {

        Page<PostEntity> postFeedPage;

        PostStatusEntity openPostStatusEntity = postStatusRepository.getById(PostStatusEnum.OPEN.getValue());

        try {
            switch (feedType) {
                case FEED_BY_TAG -> {
                    ValidFeedParamDto byTagParams = validator.validateTagId(tagIdOpt);
                    postFeedPage = postRepository
                            .findAllByTagsContainingAndPostStatusEqualsOrderByCreatedAtDesc(byTagParams.getTagEntity(), openPostStatusEntity, pageable);
                }
                default -> postFeedPage = postRepository.findAllByPostStatusEqualsOrderByCreatedAtDesc(openPostStatusEntity, pageable);
            }

        } catch (RequestParamException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }

        return postFeedPage;
    }


}

