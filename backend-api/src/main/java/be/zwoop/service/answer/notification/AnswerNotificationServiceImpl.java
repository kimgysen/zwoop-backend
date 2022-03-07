package be.zwoop.service.answer.notification;

import be.zwoop.amqp.post_notification.PostNotificationSender;
import be.zwoop.amqp.user_notification.UserNotificationSender;
import be.zwoop.domain.model.answer.AnswerDto;
import be.zwoop.domain.model.user.UserDto;
import be.zwoop.domain.post_update.PostUpdateDto;
import be.zwoop.domain.post_update.PostUpdateType;
import be.zwoop.domain.user_notification.UserNotificationDto;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static be.zwoop.domain.user_notification.UserNotificationType.*;

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

        userNotificationSender.sendUserNotification(
                UserNotificationDto.builder()
                        .user(UserDto.fromUserEntity(opEntity))
                        .userNotificationType(ANSWER_ADDED)
                        .notificationText(consultantEntity.getNickName() + " added an answer")
                        .redirectPath("/post/" + postEntity.getPostId())
                        .notificationDate(LocalDateTime.now())
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

        userNotificationSender.sendUserNotification(
                UserNotificationDto.builder()
                        .user(UserDto.fromUserEntity(opEntity))
                        .userNotificationType(ANSWER_CHANGED)
                        .notificationText(consultantEntity.getNickName() + " changed the answer")
                        .redirectPath("/post/" + postEntity.getPostId())
                        .notificationDate(LocalDateTime.now())
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


        userNotificationSender.sendUserNotification(
                UserNotificationDto.builder()
                        .user(UserDto.fromUserEntity(opEntity))
                        .userNotificationType(ANSWER_REMOVED)
                        .notificationText(consultantEntity.getNickName() + " removed the answer")
                        .redirectPath("/post/" + postEntity.getPostId())
                        .notificationDate(LocalDateTime.now())
                        .build());

        postNotificationSender.sendPostUpdateNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_REMOVED)
                        .dto(AnswerDto.fromEntity(answerEntity))
                        .build());
    }

}
