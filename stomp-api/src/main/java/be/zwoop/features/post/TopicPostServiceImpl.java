package be.zwoop.features.post;

import be.zwoop.amqp.domain.common.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.common.feature.deal.DealInitDto;
import be.zwoop.amqp.domain.post.feature.bidding.*;
import be.zwoop.amqp.domain.post.feature.post.PostChangedDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import be.zwoop.amqp.domain.post.PostUpdateDto;

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
                PostUpdateDto.builder()
                        .postId(postId)
                        .postUpdateType(POST_CHANGED)
                        .dto(postChangedDto)
                        .build());
    }

    @Override
    public void sendBiddingAdded(UUID postId, BiddingAddedDto biddingAddedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postId(postId)
                        .postUpdateType(BIDDING_ADDED)
                        .dto(biddingAddedDto)
                        .build());
    }

    @Override
    public void sendBiddingChanged(UUID postId, BiddingChangedDto biddingChangedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(BIDDING_CHANGED)
                        .postId(postId)
                        .dto(biddingChangedDto)
                        .build());
    }

    @Override
    public void sendBiddingRemoved(UUID postId, BiddingRemovedDto biddingRemovedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postId(postId)
                        .postUpdateType(BIDDING_REMOVED)
                        .dto(biddingRemovedDto)
                        .build());
    }


    @Override
    public void sendDealInit(UUID postId, DealInitDto dealInitDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(DEAL_INIT)
                        .dto(dealInitDto)
                        .build());
    }

    @Override
    public void sendDealCancelled(UUID postId, DealCancelledDto dealCancelledDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(DEAL_CANCELLED)
                        .dto(dealCancelledDto)
                        .build());
    }


    private String postUpdatesDestination(String postId) {
        return "/topic/" + postId + ".post.updates";
    }

}
