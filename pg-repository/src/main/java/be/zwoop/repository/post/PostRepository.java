package be.zwoop.repository.post;

import be.zwoop.repository.poststatus.PostStatusEntity;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    Optional<PostEntity> findByPostTitleAndOp(String title, UserEntity opId);
    Page<PostEntity> findAllByPostState_PostStatusEqualsOrderByCreatedAtDesc(PostStatusEntity postStatus, Pageable pageable);
    Page<PostEntity> findAllByTagsContainingAndPostState_PostStatusEqualsOrderByCreatedAtDesc(TagEntity tagEntity, PostStatusEntity postStatus, Pageable pageable);

}
