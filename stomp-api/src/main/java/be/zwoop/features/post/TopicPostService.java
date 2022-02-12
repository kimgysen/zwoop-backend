package be.zwoop.features.post;

import be.zwoop.amqp.domain.post.feature.*;

import java.util.UUID;

public interface TopicPostService {

    void sendPostChanged(UUID postId, PostChangedDto post);
    void sendBiddingAdded(UUID postId, BiddingAddedDto dto);
    void sendBiddingChanged(UUID postId, BiddingChangedDto dto);
    void sendBiddingRemoved(UUID postId, BiddingRemovedDto dto);
    void sendBiddingAccepted(UUID postId, BiddingAcceptedDto dto);

}
