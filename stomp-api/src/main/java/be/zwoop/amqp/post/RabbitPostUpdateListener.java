package be.zwoop.amqp.post;


import be.zwoop.amqp.domain.common.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.common.feature.deal.DealInitDto;
import be.zwoop.amqp.domain.post.PostUpdateDto;
import be.zwoop.amqp.domain.post.feature.bidding.*;
import be.zwoop.amqp.domain.post.feature.post.PostChangedDto;
import be.zwoop.features.post.TopicPostService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static be.zwoop.amqp.post.RabbitPostConfig.RABBIT_POST_UPDATES_QUEUE;

@Service
@AllArgsConstructor
public class RabbitPostUpdateListener {

    private final TopicPostService topicPostService;


    @RabbitListener(queues = RABBIT_POST_UPDATES_QUEUE, concurrency = "${rabbit.queue.postupdates.concurrent.listeners}")
    public void receiveMessage(final PostUpdateDto<?> receivedDto) {
        UUID postId = receivedDto.getPostId();

        switch (receivedDto.getPostUpdateType()) {
            case POST_CHANGED -> topicPostService.sendPostChanged(postId, (PostChangedDto) receivedDto.getDto());

            case BIDDING_ADDED -> topicPostService.sendBiddingAdded(postId, (BiddingAddedDto) receivedDto.getDto());
            case BIDDING_REMOVED -> topicPostService.sendBiddingRemoved(postId, (BiddingRemovedDto) receivedDto.getDto());
            case BIDDING_CHANGED -> topicPostService.sendBiddingChanged(postId, (BiddingChangedDto) receivedDto.getDto());
            case DEAL_INIT -> topicPostService.sendDealInit(postId, (DealInitDto) receivedDto.getDto());
            case DEAL_CANCELLED -> topicPostService.sendDealCancelled(postId, (DealCancelledDto) receivedDto.getDto());

        }
    }
}
