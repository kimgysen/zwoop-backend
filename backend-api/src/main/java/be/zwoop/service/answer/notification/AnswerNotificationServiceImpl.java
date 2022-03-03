package be.zwoop.service.answer.notification;

import be.zwoop.amqp.domain.notification.NotificationDto;
import be.zwoop.amqp.domain.notification.NotificationType;
import be.zwoop.amqp.domain.post_update.PostUpdateDto;
import be.zwoop.amqp.domain.post_update.PostUpdateType;
import be.zwoop.amqp.notification.NotificationSender;
import be.zwoop.amqp.post.PostNotificationSender;
import be.zwoop.repository.answer.AnswerEntity;
import be.zwoop.repository.post.PostEntity;
import be.zwoop.service.answer.AnswerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static be.zwoop.amqp.domain.notification.NotificationType.*;

@Service
@AllArgsConstructor
public class AnswerNotificationServiceImpl implements AnswerNotificationService {

    private final AnswerFactory answerFactory;
    private final NotificationSender notificationSender;
    private final PostNotificationSender postNotificationSender;


    @Override
    public void sendAnswerAddedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                        .notificationType(ANSWER_ADDED)
                        .dto(answerFactory.
                                buildAnswerAddedDto(answerEntity))
                        .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_ADDED)
                        .dto(answerFactory
                                .buildAnswerAddedDto(answerEntity))
                        .build());
    }

    @Override
    public void sendAnswerChangedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                        .notificationType(ANSWER_CHANGED)
                        .dto(answerFactory.
                                buildAnswerRemovedDto(answerEntity))
                        .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_CHANGED)
                        .dto(answerFactory
                                .buildAnswerChangedDto(answerEntity))
                        .build());
    }

    @Override
    public void sendAnswerRemovedNotification(AnswerEntity answerEntity) {
        PostEntity postEntity = answerEntity.getPost();

        notificationSender.sendNotification(
                NotificationDto.builder()
                        .notificationType(ANSWER_REMOVED)
                        .dto(answerFactory.
                                buildAnswerRemovedDto(answerEntity))
                        .build());

        postNotificationSender.sendPostEventNotification(
                PostUpdateDto.builder()
                        .postId(postEntity.getPostId())
                        .postUpdateType(PostUpdateType.ANSWER_REMOVED)
                        .dto(answerFactory
                                .buildAnswerRemovedDto(answerEntity))
                        .build());
    }

}
