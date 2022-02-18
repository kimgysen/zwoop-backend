package be.zwoop.amqp.post;


import be.zwoop.amqp.domain.post.PostUpdateFeatureDto;
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
    public void receiveMessage(final PostUpdateFeatureDto<?> receivedDto) {
        UUID postId = receivedDto.getPostId();

        switch (receivedDto.getPostUpdateType()) {
            case POST_CHANGED -> topicPostService.sendPostChanged(postId, (PostChangedDto) receivedDto.getPostUpdateDto());

            case BIDDING_ADDED -> topicPostService.sendBiddingAdded(postId, (BiddingAddedDto) receivedDto.getPostUpdateDto());
            case BIDDING_REMOVED -> topicPostService.sendBiddingRemoved(postId, (BiddingRemovedDto) receivedDto.getPostUpdateDto());
            case BIDDING_CHANGED -> topicPostService.sendBiddingChanged(postId, (BiddingChangedDto) receivedDto.getPostUpdateDto());
            case BIDDING_ACCEPTED -> topicPostService.sendBiddingAccepted(postId, (BiddingAcceptedDto) receivedDto.getPostUpdateDto());
            case BIDDING_REMOVE_ACCEPTED -> topicPostService.sendBiddingRemoveAccepted(postId, (BiddingRemoveAcceptedDto) receivedDto.getPostUpdateDto());

        }
    }
}
