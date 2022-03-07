package be.zwoop.amqp.user_notification;


import be.zwoop.domain.user_notification.UserNotificationDto;
import be.zwoop.features.user_notification.UserNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static be.zwoop.amqp.user_notification.RabbitNotificationConfig.RABBIT_NOTIFICATIONS_QUEUE;

@Service
@AllArgsConstructor
public class RabbitNotificationListener {
    private final UserNotificationService notificationService;

    @RabbitListener(queues = RABBIT_NOTIFICATIONS_QUEUE, concurrency = "${rabbit.queue.postupdates.concurrent.listeners}")
    public void receiveMessage(final UserNotificationDto receivedDto) {
        notificationService.sendUserNotification(receivedDto.getUser().getUserId(), receivedDto);
    }
}
