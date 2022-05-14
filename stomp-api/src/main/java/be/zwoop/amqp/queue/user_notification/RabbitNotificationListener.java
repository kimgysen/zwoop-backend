package be.zwoop.amqp.queue.user_notification;


import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.features.user_notification.UserNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static be.zwoop.amqp.queue.user_notification.RabbitNotificationConfig.RABBIT_NOTIFICATIONS_QUEUE;

@Service
@AllArgsConstructor
public class RabbitNotificationListener {
    private final UserNotificationService notificationService;

    @RabbitListener(queues = RABBIT_NOTIFICATIONS_QUEUE, concurrency = "${rabbit.queue.postupdates.concurrent.listeners}")
    public void receiveMessage(final NotificationDto<?> receivedDto) {
        switch (receivedDto.getUserNotificationType()) {
            default -> {
                // User notifications for post updates
                notificationService.sendUserNotification(receivedDto.getUserId(), receivedDto);
            }
        }
    }
}
