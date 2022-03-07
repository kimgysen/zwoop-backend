package be.zwoop.service.deal.notification;

import be.zwoop.amqp.post_notification.PostNotificationSender;
import be.zwoop.amqp.user_notification.UserNotificationSender;
import be.zwoop.domain.model.deal.DealDto;
import be.zwoop.domain.model.user.UserDto;
import be.zwoop.domain.post_update.PostUpdateDto;
import be.zwoop.domain.post_update.PostUpdateType;
import be.zwoop.domain.user_notification.UserNotificationDto;
import be.zwoop.repository.bidding.BiddingEntity;
import be.zwoop.repository.deal.DealEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static be.zwoop.domain.user_notification.UserNotificationType.DEAL_CANCELLED;
import static be.zwoop.domain.user_notification.UserNotificationType.DEAL_INIT;

@Service
@AllArgsConstructor
public class DealNotificationServiceImpl implements DealNotificationService {

    private final UserNotificationSender userNotificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendDealInitNotification(DealEntity dealEntity) {
        BiddingEntity biddingEntity = dealEntity.getBidding();
        PostEntity postEntity = biddingEntity.getPost();
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = biddingEntity.getConsultant();

        userNotificationSender.sendUserNotification(
                UserNotificationDto.builder()
                        .user(UserDto.fromUserEntity(consultantEntity))
                        .userNotificationType(DEAL_INIT)
                        .notificationText(opEntity.getNickName() + " accepted the deal.")
                        .metaText(postEntity.getPostText())
                        .redirectPath("/post/" + postEntity.getPostId())
                        .notificationDate(LocalDateTime.now())
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
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = biddingEntity.getConsultant();

        userNotificationSender.sendUserNotification(
                UserNotificationDto.builder()
                        .user(UserDto.fromUserEntity(consultantEntity))
                        .userNotificationType(DEAL_CANCELLED)
                        .notificationText(opEntity.getNickName() + " cancelled the deal.")
                        .redirectPath("/post/" + postEntity.getPostId())
                        .notificationDate(LocalDateTime.now())
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.DEAL_CANCELLED)
                        .dto(DealDto.fromEntity(dealEntity))
                        .build());
    }

}
