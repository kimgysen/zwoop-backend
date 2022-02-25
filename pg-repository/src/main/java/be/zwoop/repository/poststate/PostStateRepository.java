package be.zwoop.repository.poststate;

import be.zwoop.repository.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostStateRepository extends JpaRepository<PostStateEntity, UUID> {
    Optional<PostStateEntity> findByPost(PostEntity post);
}
