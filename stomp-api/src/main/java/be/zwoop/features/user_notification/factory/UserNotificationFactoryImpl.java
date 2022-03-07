package be.zwoop.features.user_notification.factory;

import be.zwoop.domain.user_notification.UserNotificationDto;
import be.zwoop.features.user_notification.repository.cassandra.UserNotificationEntity;
import be.zwoop.features.user_notification.repository.cassandra.UserNotificationPrimaryKey;

import java.util.Date;

public class UserNotificationFactoryImpl implements UserNotificationFactory {

    @Override
    public UserNotificationEntity buildUserNotificationEntity(UserNotificationDto userNotificationDto) {
        UserNotificationPrimaryKey pk = UserNotificationPrimaryKey.builder()
                .userId(userNotificationDto.getUser().getUserId().toString())
                .notificationDate(new Date())
                .build();

        return UserNotificationEntity.builder()
                .userNotificationType(userNotificationDto.getUserNotificationType())
                .notificationText(userNotificationDto.getNotificationText())
                .redirectPath(userNotificationDto.getRedirectPath())
                .build();
    }
}
