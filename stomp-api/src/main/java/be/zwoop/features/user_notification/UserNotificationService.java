package be.zwoop.features.user_notification;

import be.zwoop.domain.notification.queue.NotificationDto;

import java.util.UUID;

public interface UserNotificationService {
    void sendUserNotification(UUID userId, NotificationDto<?> notificationDto);

}
