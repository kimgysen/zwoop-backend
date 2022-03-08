package be.zwoop.amqp.topic.post_notification;


import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.bidding.BiddingDto;
import be.zwoop.domain.model.deal.DealDto;
import be.zwoop.domain.model.post.PostDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateDto;
import be.zwoop.features.post.TopicPostService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static be.zwoop.amqp.topic.post_notification.RabbitPostConfig.RABBIT_POST_UPDATES_QUEUE;

@Service
@AllArgsConstructor
public class RabbitPostUpdateListener {

    private final TopicPostService topicPostService;


    @RabbitListener(queues = RABBIT_POST_UPDATES_QUEUE, concurrency = "${rabbit.queue.postupdates.concurrent.listeners}")
    public void receiveMessage(final PostUpdateDto<?> receivedDto) {
        UUID postId = receivedDto.getPostId();

        switch (receivedDto.getPostUpdateType()) {
            case POST_CHANGED -> topicPostService.sendPostChanged(postId, (PostDto) receivedDto.getDto());
            case BIDDING_ADDED -> topicPostService.sendBiddingAdded(postId, (BiddingDto) receivedDto.getDto());
            case BIDDING_REMOVED -> topicPostService.sendBiddingRemoved(postId, (BiddingDto) receivedDto.getDto());
            case BIDDING_CHANGED -> topicPostService.sendBiddingChanged(postId, (BiddingDto) receivedDto.getDto());
            case DEAL_INIT -> topicPostService.sendDealInit(postId, (DealDto) receivedDto.getDto());
            case DEAL_CANCELLED -> topicPostService.sendDealCancelled(postId, (DealDto) receivedDto.getDto());
            case ANSWER_ADDED -> topicPostService.sendAnswerAdded(postId, (AnswerDto) receivedDto.getDto());
            case ANSWER_CHANGED -> topicPostService.sendAnswerChanged(postId, (AnswerDto) receivedDto.getDto());
            case ANSWER_REMOVED -> topicPostService.sendAnswerRemoved(postId, (AnswerDto) receivedDto.getDto());
        }
    }
}
