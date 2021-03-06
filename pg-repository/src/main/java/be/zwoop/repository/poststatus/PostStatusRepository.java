package be.zwoop.repository.poststatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatusRepository extends JpaRepository<PostStatusEntity, Integer> {
    PostStatusEntity findByPostStatusId(int postStatusId);
}
