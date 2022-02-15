package be.zwoop.service.bidding;

import be.zwoop.amqp.domain.post.PostUpdateFeatureDto;
import be.zwoop.amqp.domain.post.PostUpdateType;
import be.zwoop.amqp.domain.post.feature.*;
import be.zwoop.amqp.post.PostUpdateSender;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.bidding.BiddingRepository;
import be.zwoop.repository.bidding.BiddingStatusEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class BiddingServiceImpl implements BiddingService{

    private final BiddingRepository biddingRepository;
    private final PostUpdateSender postUpdateSender;

    @Override
    public Optional<BiddingEntity> findByPostAndBiddingStatus(PostEntity postEntity, BiddingStatusEntity biddingStatusEntity) {
        return biddingRepository.findByPostEqualsAndBiddingStatusEquals(
                postEntity, biddingStatusEntity);
    }

    @Override
    public Optional<BiddingEntity> findByPostAndRespondentAndBiddingStatus(PostEntity postEntity, UserEntity respondentEntity, BiddingStatusEntity biddingStatusEntity) {
        return biddingRepository.findByPostEqualsAndRespondentEqualsAndBiddingStatusEquals(
                postEntity, respondentEntity, biddingStatusEntity);
    }

    @Override
    public Optional<BiddingEntity> findByPostAndRespondent(PostEntity postEntity, UserEntity respondentEntity) {
        return biddingRepository.findByPostEqualsAndRespondentEquals(postEntity, respondentEntity);
    }

    @Override
    public void saveBidding(BiddingEntity biddingEntity) {
        biddingRepository.saveAndFlush(biddingEntity);
    }

    @Override
    public void removeBidding(BiddingEntity biddingEntity) {
        biddingRepository.delete(biddingEntity);
    };

    @Override
    public void sendBiddingAddedToQueue(BiddingEntity biddingEntity) {
        postUpdateSender.sendToQueue(
                PostUpdateFeatureDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_ADDED)
                        .postUpdateDto(
                                BiddingAddedDto.builder()
                                        .userId(biddingEntity.getRespondent().getUserId())
                                        .nickName(biddingEntity.getRespondent().getNickName())
                                        .biddingId(biddingEntity.getBiddingId())
                                        .askPrice(biddingEntity.getAskPrice())
                                        .build())
                        .build());
    }

    @Override
    public void sendBiddingChangedToQueue(BiddingEntity biddingEntity) {
        postUpdateSender.sendToQueue(
                PostUpdateFeatureDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_CHANGED)
                        .postUpdateDto(
                                BiddingChangedDto.builder()
                                        .userId(biddingEntity.getRespondent().getUserId())
                                        .nickName(biddingEntity.getRespondent().getNickName())
                                        .biddingId(biddingEntity.getBiddingId())
                                        .askPrice(biddingEntity.getAskPrice())
                                        .build())
                        .build());
    }

    @Override
    public void sendBiddingRemovedToQueue(BiddingEntity biddingEntity) {
        postUpdateSender.sendToQueue(
                PostUpdateFeatureDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_REMOVED)
                        .postUpdateDto(
                                BiddingRemovedDto.builder()
                                        .userId(biddingEntity.getRespondent().getUserId())
                                        .nickName(biddingEntity.getRespondent().getNickName())
                                        .biddingId(biddingEntity.getBiddingId())
                                        .build())
                        .build());
    }

    @Override
    public void sendBiddingAcceptedToQueue(BiddingEntity biddingEntity) {
        postUpdateSender.sendToQueue(
                PostUpdateFeatureDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_ACCEPTED)
                        .postUpdateDto(
                                BiddingAcceptedDto.builder()
                                        .biddingId(biddingEntity.getBiddingId())
                                        .userId(biddingEntity.getPost().getAsker().getUserId())
                                        .nickName(biddingEntity.getPost().getAsker().getNickName())
                                        .build())
                        .build());
    }

    @Override
    public void sendBiddingRemoveAcceptedToQueue(BiddingEntity biddingEntity) {
        postUpdateSender.sendToQueue(
            PostUpdateFeatureDto.builder()
                    .postId(biddingEntity.getPost().getPostId())
                    .postUpdateType(PostUpdateType.BIDDING_REMOVE_ACCEPTED)
                    .postUpdateDto(
                            BiddingRemoveAcceptedDto.builder()
                                .biddingId(biddingEntity.getBiddingId())
                                .userId(biddingEntity.getPost().getAsker().getUserId())
                                .nickName(biddingEntity.getPost().getAsker().getNickName())
                            .build())
                    .build()
        );
    }


}
