package be.zwoop.features.post;

import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.bidding.BiddingDto;
import be.zwoop.domain.model.deal.DealDto;
import be.zwoop.domain.model.post.PostDto;
import be.zwoop.domain.post_update.PostUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static be.zwoop.domain.post_update.PostUpdateType.*;


@AllArgsConstructor
@Service
public class TopicPostServiceImpl implements TopicPostService {

    private final SimpMessagingTemplate wsTemplate;

    @Override
    public void sendPostChanged(UUID postId, PostDto postDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postId(postId)
                        .postUpdateType(POST_CHANGED)
                        .dto(postDto)
                        .build());
    }

    @Override
    public void sendBiddingAdded(UUID postId, BiddingDto biddingDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postId(postId)
                        .postUpdateType(BIDDING_ADDED)
                        .dto(biddingDto)
                        .build());
    }

    @Override
    public void sendBiddingChanged(UUID postId, BiddingDto biddingDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(BIDDING_CHANGED)
                        .postId(postId)
                        .dto(biddingDto)
                        .build());
    }

    @Override
    public void sendBiddingRemoved(UUID postId, BiddingDto biddingDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postId(postId)
                        .postUpdateType(BIDDING_REMOVED)
                        .dto(biddingDto)
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
    public void sendAnswerAdded(UUID postId, AnswerDto answerDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(ANSWER_ADDED)
                        .dto(answerDto)
                        .build());
    }

    @Override
    public void sendAnswerChanged(UUID postId, AnswerDto answerDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(ANSWER_CHANGED)
                        .dto(answerDto)
                        .build());
    }

    @Override
    public void sendAnswerRemoved(UUID postId, AnswerDto answerDto) {
        wsTemplate.convertAndSend(
                postUpdatesDestination(postId.toString()),
                PostUpdateDto.builder()
                        .postUpdateType(ANSWER_REMOVED)
                        .dto(answerDto)
                        .build());
    }


    private String postUpdatesDestination(String postId) {
        return "/topic/" + postId + ".post.updates";
    }

}
