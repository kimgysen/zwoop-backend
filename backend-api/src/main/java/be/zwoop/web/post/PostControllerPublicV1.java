package be.zwoop.web.post;


import be.zwoop.domain.enum_type.PostFeedTypeEnum;
import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.domain.model.post.PostDto;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.post_status.PostStatusRepository;
import be.zwoop.service.post.db.PostDbService;
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

    private final PostDbService postDbService;
    private final PostControllerValidationHelper validator;
    private final PostStatusRepository postStatusRepository;


    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID postId) {
        Optional<PostEntity> postEntityOpt = postDbService.findByPostId(postId);

        if (postEntityOpt.isPresent()) {
            return ok(PostDto.fromEntity(postEntityOpt.get()));

        } else {
            throw new ResponseStatusException(NOT_FOUND);

        }
    }

    @GetMapping
    public Page<PostDto> getPosts(
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
                    postFeedPage = postDbService
                            .getFeedByTag(byTagParams.getTagEntity(), postStatusEntity, pageable);
                }
                default -> postFeedPage = postDbService.getFeed(postStatusEntity, pageable);
            }

        } catch (RequestParamException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }

        return PostDto.fromEntityPage(postFeedPage);
    }
}
