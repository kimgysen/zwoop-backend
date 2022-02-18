package be.zwoop.repository.post;

import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    Optional<PostEntity> findByPostTitleAndAsker(String title, UserEntity askerId);
    Page<PostEntity> findAllByPostStatusEqualsOrderByCreatedAtDesc(PostStatusEntity postStatusEntity, Pageable pageable);
    Page<PostEntity> findAllByTagsContainingAndPostStatusEqualsOrderByCreatedAtDesc(TagEntity tagEntity, PostStatusEntity postStatusEntity, Pageable pageable);


}
