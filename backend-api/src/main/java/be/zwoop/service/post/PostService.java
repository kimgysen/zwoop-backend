package be.zwoop.service.post;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Optional<UserEntity> findByUserId(UUID principalId);
    Optional<PostEntity> findByTitleAndOp(String title, UserEntity opEntity);
    Optional<PostEntity> findByPostId(UUID postId);

    Page<PostEntity> getFeed(PostStatusEntity postStatus, Pageable pageable);
    Page<PostEntity> getFeedByTag(TagEntity tagEntity, PostStatusEntity postStatus, Pageable pageable);

    PostEntity createPost(PostDto postDto, UserEntity opEntity);
    void updatePost(PostEntity toUpdate, PostDto postDto);
    boolean hasPostChanged(PostEntity postEntity, PostDto postDto);

    void sendPostChangedNotification(PostEntity postEntity);
}
