package be.zwoop.amqp.post;


import be.zwoop.amqp.domain.post.PostUpdateFeatureDto;
import be.zwoop.amqp.domain.post.feature.*;
import be.zwoop.features.post.TopicPostService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static be.zwoop.amqp.post.RabbitPostConfig.RABBIT_POST_UPDATES_QUEUE;

@Service
@AllArgsConstructor
public class RabbitPostUpdateListener {

    private final TopicPostService topicPostService;


    @RabbitListener(queues = RABBIT_POST_UPDATES_QUEUE, concurrency = "${rabbit.queue.postupdates.concurrent.listeners}")
    public void receiveMessage(final PostUpdateFeatureDto<?> receivedDto) {

        switch (receivedDto.getPostUpdateType()) {
            case POST_CHANGED -> topicPostService.sendPostChanged(receivedDto.getPostId(), (PostChangedDto) receivedDto.getPostUpdateDto());
            case BIDDING_ADDED -> topicPostService.sendBiddingAdded(receivedDto.getPostId(), (BiddingAddedDto) receivedDto.getPostUpdateDto());
            case BIDDING_REMOVED -> topicPostService.sendBiddingRemoved(receivedDto.getPostId(), (BiddingRemovedDto) receivedDto.getPostUpdateDto());
            case BIDDING_CHANGED -> topicPostService.sendBiddingChanged(receivedDto.getPostId(), (BiddingChangedDto) receivedDto.getPostUpdateDto());
            case BIDDING_ACCEPTED -> topicPostService.sendBiddingAccepted(receivedDto.getPostId(), (BiddingAcceptedDto) receivedDto.getPostUpdateDto());
        }
    }
}
