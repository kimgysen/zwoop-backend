package be.zwoop.service.state_manager;

import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.post.PostEntity;

public interface StateManager {

    boolean canPostBeUpdated(PostEntity postEntity);
    void acceptBidding(PostEntity postEntity, BiddingEntity biddingEntity);
    void removeAcceptBidding(PostEntity postEntity, BiddingEntity biddingEntity);
    void answerPost(PostEntity postEntity);
    void removeAnswerPost(PostEntity postEntity);

}
