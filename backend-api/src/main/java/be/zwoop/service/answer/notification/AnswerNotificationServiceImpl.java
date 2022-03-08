package be.zwoop.service.answer.notification;

import be.zwoop.amqp.queue.user_notification.UserNotificationSender;
import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.user.UserDto;
import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.domain.notification.queue.UserNotificationType;
import be.zwoop.domain.notification.queue.user_notification.UserNotificationDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateType;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static be.zwoop.domain.notification.queue.UserNotificationType.ANSWER_ADDED;
import static be.zwoop.domain.notification.queue.UserNotificationType.ANSWER_REMOVED;

@Service
@AllArgsConstructor
public class AnswerNotificationServiceImpl implements AnswerNotificationService {

    private final UserNotificationSender userNotificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendAnswerAddedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = answerEntity.getConsultant();

        UserNotificationDto userNotificationDto = UserNotificationDto.builder()
                .user(UserDto.fromUserEntity(opEntity))
                .notificationType(UserNotificationType.ANSWER_ADDED.name())
                .notificationText(consultantEntity.getNickName() + " added an answer")
                .redirectPath("/post/" + postEntity.getPostId())
                .notificationDate(LocalDateTime.now())
                .build();

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(ANSWER_ADDED)
                        .dto(userNotificationDto)
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_ADDED)
                        .dto(AnswerDto.fromEntity(answerEntity))
                        .build());
    }

    @Override
    public void sendAnswerChangedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = answerEntity.getConsultant();

        UserNotificationDto userNotificationDto = UserNotificationDto.builder()
                .user(UserDto.fromUserEntity(opEntity))
                .notificationType(UserNotificationType.ANSWER_CHANGED.name())
                .notificationText(consultantEntity.getNickName() + " changed the answer")
                .redirectPath("/post/" + postEntity.getPostId())
                .notificationDate(LocalDateTime.now())
                .build();

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(ANSWER_ADDED)
                        .dto(userNotificationDto)
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_CHANGED)
                        .dto(AnswerDto.fromEntity(answerEntity))
                        .build());
    }

    @Override
    public void sendAnswerRemovedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        UserEntity opEntity = postEntity.getOp();
        UserEntity consultantEntity = answerEntity.getConsultant();

        UserNotificationDto userNotificationDto = UserNotificationDto.builder()
                .user(UserDto.fromUserEntity(opEntity))
                .notificationType(UserNotificationType.ANSWER_REMOVED.name())
                .notificationText(consultantEntity.getNickName() + " removed the answer")
                .redirectPath("/post/" + postEntity.getPostId())
                .notificationDate(LocalDateTime.now())
                .build();

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(ANSWER_REMOVED)
                        .dto(userNotificationDto)
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_REMOVED)
                        .dto(AnswerDto.fromEntity(answerEntity))
                        .build());
    }

}
