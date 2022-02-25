package be.zwoop.features.post;

import be.zwoop.amqp.domain.common.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.common.feature.deal.DealInitDto;
import be.zwoop.amqp.domain.post.feature.bidding.*;
import be.zwoop.amqp.domain.post.feature.post.PostChangedDto;

import java.util.UUID;

public interface TopicPostService {

    void sendPostChanged(UUID postId, PostChangedDto post);

    void sendBiddingAdded(UUID postId, BiddingAddedDto biddingAddedDto);
    void sendBiddingChanged(UUID postId, BiddingChangedDto biddingChangedDto);
    void sendBiddingRemoved(UUID postId, BiddingRemovedDto biddingRemovedDto);
    void sendDealInit(UUID postId, DealInitDto dealInitDto);
    void sendDealCancelled(UUID postId, DealCancelledDto dealCancelledDto);

}
