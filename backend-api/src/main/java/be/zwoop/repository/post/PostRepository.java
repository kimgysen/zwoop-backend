package be.zwoop.repository.post;

import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    Optional<PostEntity> findByAskerAndPostTitle(UserEntity askerId, String title);

    Page<PostEntity> findAllByPostStatusEqualsOrderByCreatedAtDesc(PostStatusEntity postStatusEntity, Pageable pageable);

    Page<PostEntity> findAllByTagsContainingAndPostStatusEqualsOrderByCreatedAtDesc(TagEntity tagEntity, PostStatusEntity postStatusEntity, Pageable pageable);

}
