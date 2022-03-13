package be.zwoop.service.usernotification.db;

import be.zwoop.repository.user.UserEntity;

public interface UserNotificationCountDbService {
    int getUnreadCount(UserEntity principal);
    void incrementUnreadCount(UserEntity userEntity);
    void resetUnreadCount(UserEntity principal);
}
