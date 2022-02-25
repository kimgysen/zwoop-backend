package be.zwoop.repository.bidding;

import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BiddingRepository extends JpaRepository<BiddingEntity, UUID> {

    Optional<BiddingEntity> findByPostEqualsAndConsultantEquals(PostEntity post, UserEntity consultant);
    List<BiddingEntity> findByPostEquals(PostEntity post);

}
