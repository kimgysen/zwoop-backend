package be.zwoop.service.bidding.db;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BiddingDbService {
    Optional<BiddingEntity> findByBiddingId(UUID biddingId);
    Optional<BiddingEntity> findByPostAndConsultant(PostEntity postEntity, UserEntity consultantEntity);
    List<BiddingEntity> findByPost(PostEntity postEntity);
    BiddingEntity saveBidding(BiddingEntity biddingEntity);
    void removeBidding(BiddingEntity biddingEntity);

}
