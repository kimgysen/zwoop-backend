package be.zwoop.web.post;


import be.zwoop.domain.enum_type.PostFeedTypeEnum;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post.PostRepository;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.post_status.PostStatusRepository;
import be.zwoop.service.post.PostService;
import be.zwoop.web.exception.RequestParamException;
import be.zwoop.web.post.dto.ValidFeedParamDto;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/post")
public class PostControllerPublicV1 {

    private final PostService postService;
    private final PostControllerValidationHelper validator;
    private final PostStatusRepository postStatusRepository;


    @GetMapping("/{postId}")
    public ResponseEntity<PostEntity> getPost(@PathVariable UUID postId) {
        Optional<PostEntity> postEntity = postService.findByPostId(postId);

        if (postEntity.isPresent()) {
            return ok(postEntity.get());

        } else {
            throw new ResponseStatusException(NOT_FOUND);

        }
    }

    @GetMapping
    public Page<PostEntity> getPosts(
            @RequestParam(value = "feedType") PostFeedTypeEnum feedType,
            @RequestParam(value = "postStatus") PostStatusEnum postStatusEnum,
            @RequestParam(value = "tagName") Optional<String> tagNameOpt,
            @NotNull final Pageable pageable) {

        Page<PostEntity> postFeedPage;

        PostStatusEntity postStatusEntity = postStatusRepository.getById(postStatusEnum.getValue());

        try {
            switch (feedType) {
                case FEED_BY_TAG -> {
                    ValidFeedParamDto byTagParams = validator.validateTagName(tagNameOpt);
                    postFeedPage = postService
                            .getFeedByTag(byTagParams.getTagEntity(), postStatusEntity, pageable);
                }
                default -> postFeedPage = postService.getFeed(postStatusEntity, pageable);
            }

        } catch (RequestParamException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }

        return postFeedPage;
    }
}
