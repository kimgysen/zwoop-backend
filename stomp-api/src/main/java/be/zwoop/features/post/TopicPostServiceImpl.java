package be.zwoop.features.post;

import be.zwoop.amqp.domain.post.feature.bidding.*;
import be.zwoop.amqp.domain.notification.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.notification.feature.deal.DealOpenedDto;
import be.zwoop.amqp.domain.post.feature.post.PostChangedDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import be.zwoop.amqp.domain.post.PostUpdateFeatureDto;

import java.util.UUID;

import static be.zwoop.amqp.domain.post.PostUpdateType.*;


@AllArgsConstructor
@Service
public class TopicPostServiceImpl implements TopicPostService {

    private final SimpMessagingTemplate wsTemplate;

    @Override
    public void sendPostChanged(UUID postId, PostChangedDto postChangedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateFeatureDto.builder()
                        .postId(postId)
                        .postUpdateType(POST_CHANGED)
                        .postUpdateDto(postChangedDto)
                        .build());
    }

    @Override
    public void sendBiddingAdded(UUID postId, BiddingAddedDto biddingAddedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateFeatureDto.builder()
                        .postUpdateType(BIDDING_ADDED)
                        .postUpdateDto(biddingAddedDto)
                        .build());
    }

    @Override
    public void sendBiddingChanged(UUID postId, BiddingChangedDto biddingChangedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateFeatureDto.builder()
                        .postUpdateType(BIDDING_CHANGED)
                        .postUpdateDto(biddingChangedDto)
                        .build());
    }

    @Override
    public void sendBiddingRemoved(UUID postId, BiddingRemovedDto biddingRemovedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateFeatureDto.builder()
                        .postUpdateType(BIDDING_REMOVED)
                        .postUpdateDto(biddingRemovedDto)
                        .build());
    }

    @Override
    public void sendBiddingAccepted(UUID postId, BiddingAcceptedDto biddingAcceptedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateFeatureDto.builder()
                        .postUpdateType(BIDDING_ACCEPTED)
                        .postUpdateDto(biddingAcceptedDto)
                        .build());
    }

    @Override
    public void sendBiddingRemoveAccepted(UUID postId, BiddingRemoveAcceptedDto biddingRemoveAcceptedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateFeatureDto.builder()
                        .postUpdateType(BIDDING_REMOVE_ACCEPTED)
                        .postUpdateDto(biddingRemoveAcceptedDto)
                        .build());
    }

    private String postUpdatesDestination(String postId) {
        return "/topic/" + postId + ".post.updates";
    }

}
