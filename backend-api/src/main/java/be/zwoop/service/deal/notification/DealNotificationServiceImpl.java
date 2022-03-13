package be.zwoop.service.deal.notification;

import be.zwoop.amqp.queue.user_notification.UserNotificationSender;
import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.domain.model.deal.DealDto;
import be.zwoop.domain.model.usernotification.UserNotificationDto;
import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.domain.notification.queue.UserNotificationType;
import be.zwoop.domain.notification.topic.post_update.PostUpdateDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateType;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import be.zwoop.service.usernotification.db.UserNotificationDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DealNotificationServiceImpl implements DealNotificationService {

    private final UserNotificationDbService userNotificationDbService;
    private final UserNotificationSender userNotificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendDealInitNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();
        UserEntity consultantEntity = biddingEntity.getConsultant();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveDealInitNotification(dealEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(consultantEntity.getUserId())
                        .userNotificationType(UserNotificationType.POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_INIT)
                        .dto(DealDto.fromEntity(dealEntity))
                        .build());
    }

    @Override
    public void sendDealRemovedNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();
        UserEntity consultantEntity = biddingEntity.getConsultant();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveDealCancelledNotification(dealEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(consultantEntity.getUserId())
                        .userNotificationType(UserNotificationType.POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_CANCELLED)
                        .dto(DealDto.fromEntity(dealEntity))
                        .build());
    }

}
