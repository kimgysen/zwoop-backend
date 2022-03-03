package be.zwoop.features.post;

import be.zwoop.amqp.domain.model.DealDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerAddedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerChangedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerRemovedDto;
import be.zwoop.amqp.domain.post_update.feature.bidding.BiddingAddedDto;
import be.zwoop.amqp.domain.post_update.feature.bidding.BiddingChangedDto;
import be.zwoop.amqp.domain.post_update.feature.bidding.BiddingRemovedDto;
import be.zwoop.amqp.domain.post_update.feature.post.PostChangedDto;

import java.util.UUID;

public interface TopicPostService {
    void sendPostChanged(UUID postId, PostChangedDto post);

    void sendBiddingAdded(UUID postId, BiddingAddedDto biddingAddedDto);
    void sendBiddingChanged(UUID postId, BiddingChangedDto biddingChangedDto);
    void sendBiddingRemoved(UUID postId, BiddingRemovedDto biddingRemovedDto);
    void sendDealInit(UUID postId, DealDto dealInitDto);
    void sendDealCancelled(UUID postId, DealDto dealCancelledDto);
    void sendAnswerAdded(UUID postId, AnswerAddedDto answerAddedDto);
    void sendAnswerChanged(UUID postId, AnswerChangedDto answerChangedDto);
    void sendAnswerRemoved(UUID postId, AnswerRemovedDto answerRemovedDto);
}
