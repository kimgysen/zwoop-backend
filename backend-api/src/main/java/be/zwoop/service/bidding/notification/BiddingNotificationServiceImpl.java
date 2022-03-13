package be.zwoop.service.bidding.notification;

import be.zwoop.amqp.queue.user_notification.UserNotificationSender;
import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.domain.model.bidding.BiddingDto;
import be.zwoop.domain.model.usernotification.UserNotificationDto;
import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.domain.notification.queue.UserNotificationType;
import be.zwoop.domain.notification.topic.post_update.PostUpdateDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateType;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import be.zwoop.service.usernotification.db.UserNotificationDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class BiddingNotificationServiceImpl implements BiddingNotificationService {

    private final UserNotificationDbService userNotificationDbService;
    private final PostNotificationSender postNotificationSender;
    private final UserNotificationSender userNotificationSender;


    @Override
    public void sendBiddingAddedNotification(BiddingEntity biddingEntity) {
        UserEntity opEntity = biddingEntity.getPost().getOp();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveBiddingAddedNotification(biddingEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(UserNotificationType.POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_ADDED)
                        .dto(BiddingDto.fromEntity(biddingEntity))
                        .build());
    }

    @Override
    public void sendBiddingChangedNotification(BiddingEntity biddingEntity) {
        UserEntity opEntity = biddingEntity.getPost().getOp();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveBiddingChangedNotification(biddingEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(UserNotificationType.POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_REMOVED)
                        .dto(BiddingDto.fromEntity(biddingEntity))
                        .build());
    }

    @Override
    public void sendBiddingRemovedNotification(BiddingEntity biddingEntity) {
        UserEntity opEntity = biddingEntity.getPost().getOp();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveBiddingRemoveNotification(biddingEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(UserNotificationType.POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_REMOVED)
                        .dto(BiddingDto.fromEntity(biddingEntity))
                        .build());
    }

}
