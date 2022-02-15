package be.zwoop.service.bidding;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingStatusEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;

import java.util.Optional;

public interface BiddingService {
    Optional<BiddingEntity> findByPostAndBiddingStatus(PostEntity postEntity, BiddingStatusEntity biddingStatusEntity);
    Optional<BiddingEntity> findByPostAndRespondentAndBiddingStatus(PostEntity postEntity, UserEntity respondentEntity, BiddingStatusEntity biddingStatusEntity);
    Optional<BiddingEntity> findByPostAndRespondent(PostEntity postEntity, UserEntity respondentEntity);
    void saveBidding(BiddingEntity biddingEntity);
    void removeBidding(BiddingEntity biddingEntity);

    void sendBiddingAddedToQueue(BiddingEntity biddingEntity);
    void sendBiddingChangedToQueue(BiddingEntity biddingEntity);
    void sendBiddingRemovedToQueue(BiddingEntity biddingEntity);
    void sendBiddingAcceptedToQueue(BiddingEntity biddingEntity);
    void sendBiddingRemoveAcceptedToQueue(BiddingEntity biddingEntity);
}
