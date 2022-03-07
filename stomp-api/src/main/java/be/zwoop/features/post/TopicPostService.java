package be.zwoop.features.post;

import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.bidding.BiddingDto;
import be.zwoop.domain.model.deal.DealDto;
import be.zwoop.domain.model.post.PostDto;

import java.util.UUID;

public interface TopicPostService {
    void sendPostChanged(UUID postId, PostDto postDto);

    void sendBiddingAdded(UUID postId, BiddingDto biddingDto);
    void sendBiddingChanged(UUID postId, BiddingDto biddingDto);
    void sendBiddingRemoved(UUID postId, BiddingDto biddingDto);
    void sendDealInit(UUID postId, DealDto dealDto);
    void sendDealCancelled(UUID postId, DealDto dealDto);
    void sendAnswerAdded(UUID postId, AnswerDto answerDto);
    void sendAnswerChanged(UUID postId, AnswerDto answerDto);
    void sendAnswerRemoved(UUID postId, AnswerDto answerDto);
}
