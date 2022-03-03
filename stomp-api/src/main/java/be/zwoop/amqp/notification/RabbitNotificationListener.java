package be.zwoop.amqp.notification;


import be.zwoop.amqp.domain.notification.NotificationDto;
import be.zwoop.amqp.domain.notification.feature.deal.DealCancelledDto;
import be.zwoop.amqp.domain.notification.feature.deal.DealInitDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerAddedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerChangedDto;
import be.zwoop.amqp.domain.post_update.feature.answer.AnswerRemovedDto;
import be.zwoop.features.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static be.zwoop.amqp.notification.RabbitNotificationConfig.RABBIT_NOTIFICATIONS_QUEUE;

@Service
@AllArgsConstructor
public class RabbitNotificationListener {
    private final NotificationService notificationService;


    @RabbitListener(queues = RABBIT_NOTIFICATIONS_QUEUE, concurrency = "${rabbit.queue.postupdates.concurrent.listeners}")
    public void receiveMessage(final NotificationDto<?> receivedDto) {
        switch (receivedDto.getNotificationType()) {
            case DEAL_INIT -> {
                DealInitDto dealInitDto = (DealInitDto) receivedDto.getDto();
                notificationService.sendNotification(dealInitDto.getOp().getUserId(), receivedDto);
                notificationService.sendNotification(dealInitDto.getConsultant().getUserId(), receivedDto);
            }
            case DEAL_CANCELLED -> {
                DealCancelledDto dealCancelledDto = (DealCancelledDto) receivedDto.getDto();
                notificationService.sendNotification(dealCancelledDto.getOp().getUserId(), receivedDto);
                notificationService.sendNotification(dealCancelledDto.getConsultant().getUserId(), receivedDto);
            }
            case ANSWER_ADDED -> {
                AnswerAddedDto answerAddedDto = (AnswerAddedDto) receivedDto.getDto();
                notificationService.sendNotification(answerAddedDto.getOp().getUserId(), receivedDto);
                notificationService.sendNotification(answerAddedDto.getConsultant().getUserId(), receivedDto);
            }
            case ANSWER_CHANGED -> {
                AnswerChangedDto answerChangedDto = (AnswerChangedDto) receivedDto.getDto();
                notificationService.sendNotification(answerChangedDto.getOp().getUserId(), receivedDto);
                notificationService.sendNotification(answerChangedDto.getConsultant().getUserId(), receivedDto);
            }
            case ANSWER_REMOVED -> {
                AnswerRemovedDto answerRemovedDto = (AnswerRemovedDto) receivedDto.getDto();
                notificationService.sendNotification(answerRemovedDto.getOp().getUserId(), receivedDto);
                notificationService.sendNotification(answerRemovedDto.getConsultant().getUserId(), receivedDto);
            }
        }
    }
}
