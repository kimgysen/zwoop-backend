package be.zwoop.service.answer.notification;

import be.zwoop.amqp.queue.user_notification.UserNotificationSender;
import be.zwoop.amqp.topic.post_notification.PostNotificationSender;
import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.usernotification.UserNotificationDto;
import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateDto;
import be.zwoop.domain.notification.topic.post_update.PostUpdateType;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import be.zwoop.service.usernotification.db.UserNotificationDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static be.zwoop.domain.notification.queue.UserNotificationType.POST_NOTIFICATION;

@Service
@AllArgsConstructor
public class AnswerNotificationServiceImpl implements AnswerNotificationService {

    private final UserNotificationDbService userNotificationDbService;
    private final UserNotificationSender userNotificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendAnswerAddedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        UserEntity opEntity = postEntity.getOp();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveAnswerAddedNotification(answerEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
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

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveAnswerChangedNotification(answerEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
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

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveAnswerRemovedNotification(answerEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(opEntity.getUserId())
                        .userNotificationType(POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_REMOVED)
                        .dto(AnswerDto.fromEntity(answerEntity))
                        .build());
    }

    @Override
    public void sendAnswerAcceptedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();
        UserEntity consultantEntity = answerEntity.getConsultant();

        UserNotificationEntity userNotificationEntity = userNotificationDbService.saveAnswerAcceptedNotification(answerEntity);

        userNotificationSender.sendUserNotification(
                NotificationDto.builder()
                        .userId(consultantEntity.getUserId())
                        .userNotificationType(POST_NOTIFICATION)
                        .dto(UserNotificationDto.fromEntity(userNotificationEntity))
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_ACCEPTED)
                        .dto(AnswerDto.fromEntity(answerEntity))
                        .build());
    }

}
