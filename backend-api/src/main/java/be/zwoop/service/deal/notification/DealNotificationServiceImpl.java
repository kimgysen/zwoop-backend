package be.zwoop.service.deal.notification;

import be.zwoop.amqp.domain.notification.NotificationDto;
import be.zwoop.amqp.domain.post_update.PostUpdateDto;
import be.zwoop.amqp.domain.post_update.PostUpdateType;
import be.zwoop.amqp.notification.NotificationSender;
import be.zwoop.amqp.post.PostNotificationSender;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.service.deal.DealFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static be.zwoop.amqp.domain.notification.NotificationType.DEAL_CANCELLED;
import static be.zwoop.amqp.domain.notification.NotificationType.DEAL_INIT;

@Service
@AllArgsConstructor
public class DealNotificationServiceImpl implements DealNotificationService {

    private final DealFactory dealFactory;
    private final NotificationSender notificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendDealInitNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                    .notificationType(DEAL_INIT)
                    .dto(dealFactory
                            .buildDealInitDto(dealEntity))
                    .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_INIT)
                        .dto(dealFactory
                                .buildDealDto(dealEntity))
                        .build());
    }

    @Override
    public void sendDealRemovedNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                        .notificationType(DEAL_CANCELLED)
                        .dto(dealFactory.
                                buildDealCancelledDto(dealEntity))
                        .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_CANCELLED)
                        .dto(dealFactory
                                .buildDealDto(dealEntity))
                        .build());
    }

}
