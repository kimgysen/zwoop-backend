package be.zwoop.features.user_notification.factory;

import be.zwoop.domain.user_notification.UserNotificationDto;
import be.zwoop.features.user_notification.repository.cassandra.UserNotificationEntity;

public interface UserNotificationFactory {
    UserNotificationEntity buildUserNotificationEntity(UserNotificationDto userNotificationDto);

}
