package be.zwoop.service.bidding.notification;

import be.zwoop.amqp.domain.post_update.PostUpdateDto;
import be.zwoop.amqp.domain.post_update.PostUpdateType;
import be.zwoop.amqp.post.PostNotificationSender;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.service.bidding.BiddingFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BiddingNotificationServiceImpl implements BiddingNotificationService {

    private final BiddingFactory biddingFactory;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendBiddingAddedNotification(BiddingEntity biddingEntity) {
        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_ADDED)
                        .dto(biddingFactory
                                .buildBiddingAddedDto(biddingEntity))
                        .build());
    }

    @Override
    public void sendBiddingChangedNotification(BiddingEntity biddingEntity) {
        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_CHANGED)
                        .dto(biddingFactory
                                .buildBiddingChangedDto(biddingEntity))
                        .build());
    }

    @Override
    public void sendBiddingRemovedNotification(BiddingEntity biddingEntity) {
        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_REMOVED)
                        .dto(biddingFactory
                                .buildBiddingRemovedDto(biddingEntity))
                        .build());
    }

}
