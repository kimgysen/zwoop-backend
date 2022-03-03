package be.zwoop.service.answer.notification;

import be.zwoop.repository.answer.AnswerEntity;

public interface AnswerNotificationService {
    void sendAnswerAddedNotification(AnswerEntity answerEntity);
    void sendAnswerChangedNotification(AnswerEntity answerEntity);
    void sendAnswerRemovedNotification(AnswerEntity answerEntity);

}
