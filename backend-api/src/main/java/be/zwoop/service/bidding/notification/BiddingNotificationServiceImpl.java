package be.zwoop.service.bidding.notification;

import be.zwoop.amqp.queue.user_notification.UserNotificationSender;
import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.domain.model.bidding.BiddingDto;
import be.zwoop.domain.model.user.UserDto;
import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.domain.notification.queue.UserNotificationType;
import be.zwoop.domain.notification.queue.user_notification.UserNotificationDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateType;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@AllArgsConstructor
@Component
public class BiddingNotificationServiceImpl implements BiddingNotificationService {

    private final PostNotificationSender postNotificationSender;
    private final UserNotificationSender userNotificationSender;


    @Override
    public void sendBiddingAddedNotification(BiddingEntity biddingEntity) {
        UserEntity opEntity = biddingEntity.getPost().getOp();
        UserEntity consultantEntity = biddingEntity.getConsultant();

        UserNotificationDto userNotificationDto = UserNotificationDto.builder()
                .user(UserDto.fromUserEntity(opEntity))
                .notificationType(UserNotificationType.BIDDING_ADDED.name())
                .notificationText(consultantEntity.getNickName() + " added a bidding")
                .redirectPath("/post/" + biddingEntity.getPost().getPostId())
                .notificationDate(LocalDateTime.now())
                .build();

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(UserNotificationType.BIDDING_ADDED)
                        .dto(userNotificationDto)
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
        UserEntity consultantEntity = biddingEntity.getConsultant();

        UserNotificationDto userNotificationDto = UserNotificationDto.builder()
                .user(UserDto.fromUserEntity(opEntity))
                .notificationType(UserNotificationType.BIDDING_CHANGED.name())
                .notificationText(consultantEntity.getNickName() + " changed the bidding price")
                .redirectPath("/post/" + biddingEntity.getPost().getPostId())
                .notificationDate(LocalDateTime.now())
                .build();

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(UserNotificationType.BIDDING_CHANGED)
                        .dto(userNotificationDto)
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
        UserEntity consultantEntity = biddingEntity.getConsultant();

        UserNotificationDto userNotificationDto = UserNotificationDto.builder()
                .user(UserDto.fromUserEntity(opEntity))
                .notificationType(UserNotificationType.BIDDING_REMOVED.name())
                .notificationText(consultantEntity.getNickName() + " deleted the bidding")
                .redirectPath("/post/" + biddingEntity.getPost().getPostId())
                .notificationDate(LocalDateTime.now())
                .build();

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(UserNotificationType.BIDDING_REMOVED)
                        .dto(userNotificationDto)
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(biddingEntity.getPost().getPostId())
                        .postUpdateType(PostUpdateType.BIDDING_REMOVED)
                        .dto(BiddingDto.fromEntity(biddingEntity))
                        .build());
    }

}
