package be.zwoop.features.user_notification;

import be.zwoop.domain.user_notification.UserNotificationDto;

import java.util.UUID;

public interface UserNotificationService {
    void sendUserNotification(UUID userId, UserNotificationDto userNotificationDto);

}
