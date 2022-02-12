package be.zwoop.repository.bidding;

import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BiddingRepository extends JpaRepository<BiddingEntity, UUID> {

    Optional<BiddingEntity> findByPostEqualsAndRespondentEqualsAndBiddingStatusEquals(PostEntity post, UserEntity respondent, BiddingStatusEntity biddingStatus);
    Optional<BiddingEntity> findByPostEqualsAndBiddingStatusEquals(PostEntity post, BiddingStatusEntity biddingStatus);
    Optional<BiddingEntity> findByPostEqualsAndRespondentEquals(PostEntity post, UserEntity respondent);

    List<BiddingEntity> findAllByPostEquals(PostEntity post);

}
