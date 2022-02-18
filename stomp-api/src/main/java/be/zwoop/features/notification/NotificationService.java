package be.zwoop.features.notification;

import be.zwoop.amqp.domain.notification.NotificationDto;

import java.util.UUID;

public interface NotificationService {
    void sendNotification(UUID userId, NotificationDto<?> notificationDto);

}
