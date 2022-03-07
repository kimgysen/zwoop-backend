package be.zwoop.service.post.db;

import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.post_status.PostStatusEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.web.post.dto.SavePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PostDbService {
    Optional<UserEntity> findByUserId(UUID principalId);
    Optional<PostEntity> findByTitleAndOp(String title, UserEntity opEntity);
    Optional<PostEntity> findByPostId(UUID postId);

    Page<PostEntity> getFeed(PostStatusEntity postStatus, Pageable pageable);
    Page<PostEntity> getFeedByTag(TagEntity tagEntity, PostStatusEntity postStatus, Pageable pageable);

    PostEntity createPost(SavePostDto savePostDto, UserEntity opEntity);
    void updatePost(PostEntity toUpdate, SavePostDto savePostDto);
    boolean hasPostChanged(PostEntity postEntity, SavePostDto savePostDto);

}
