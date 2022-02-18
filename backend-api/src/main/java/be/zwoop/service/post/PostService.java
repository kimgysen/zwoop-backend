package be.zwoop.service.post;

import be.zwoop.domain.enum_type.PostStatusEnum;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.PostDto;

import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Optional<UserEntity> findAskerByUserId(UUID principalId);
    Optional<PostEntity> findByTitleAndAsker(String title, UserEntity askerEntity);
    Optional<PostEntity> findByPostId(UUID postId);
    PostEntity createPost(PostDto postDto, UserEntity askerEntity);
    void updatePost(PostEntity toUpdate, PostDto postDto);
    boolean hasPostChanged(PostEntity postEntity, PostDto postDto);
    void updatePostStatus(PostEntity postEntity, PostStatusEnum postStatus);

    void sendPostChangedNotification(PostEntity postEntity);
}
