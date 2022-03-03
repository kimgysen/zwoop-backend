package be.zwoop.features.post;

import be.zwoop.amqp.domain.model.DealDto;
import be.zwoop.amqp.domain.post_update.PostUpdateDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerAddedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerChangedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerRemovedDto;
import be.zwoop.amqp.domain.post_update.feature.bidding.BiddingAddedDto;
import be.zwoop.amqp.domain.post_update.feature.bidding.BiddingChangedDto;
import be.zwoop.amqp.domain.post_update.feature.bidding.BiddingRemovedDto;
import be.zwoop.amqp.domain.post_update.feature.post.PostChangedDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static be.zwoop.amqp.domain.post_update.PostUpdateType.*;


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
    public void sendDealInit(UUID postId, DealDto dealDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(DEAL_INIT)
                        .dto(dealDto)
                        .build());
    }

    @Override
    public void sendDealCancelled(UUID postId, DealDto dealDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(DEAL_CANCELLED)
                        .dto(dealDto)
                        .build());
    }

    @Override
    public void sendAnswerAdded(UUID postId, AnswerAddedDto answerAddedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(ANSWER_ADDED)
                        .dto(answerAddedDto)
                        .build());
    }

    @Override
    public void sendAnswerChanged(UUID postId, AnswerChangedDto answerChangedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(ANSWER_CHANGED)
                        .dto(answerChangedDto)
                        .build());
    }

    @Override
    public void sendAnswerRemoved(UUID postId, AnswerRemovedDto answerRemovedDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(ANSWER_REMOVED)
                        .dto(answerRemovedDto)
                        .build());
    }


    private String postUpdatesDestination(String postId) {
        return "/topic/" + postId + ".post.updates";
    }

}
