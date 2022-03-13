package be.zwoop.service.usernotification.db;

import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationCountEntity;
import be.zwoop.repository.usernotification.UserNotificationCountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class UserNotificationCountDbServiceImpl implements UserNotificationCountDbService {

    private final UserNotificationCountRepository userNotificationCountRepository;

    @Override
    public int getUnreadCount(UserEntity principal) {
        Optional<UserNotificationCountEntity> userNotificationCountEntityOpt = userNotificationCountRepository.findById(principal.getUserId());
        return userNotificationCountEntityOpt.isEmpty()
                ? 0
                : userNotificationCountEntityOpt.get().getUnreadCount();
    }

    @Override
    public void incrementUnreadCount(UserEntity user) {
        Optional<UserNotificationCountEntity> userNotificationCountEntityOpt = userNotificationCountRepository.findById(user.getUserId());

        UserNotificationCountEntity userNotificationCountEntity;
        if (userNotificationCountEntityOpt.isEmpty()) {
            userNotificationCountEntity = UserNotificationCountEntity.builder()
                    .userId(user.getUserId())
                    .unreadCount(1)
                    .build();
        } else {
            userNotificationCountEntity = userNotificationCountEntityOpt.get();
            int unreadCount = userNotificationCountEntity.getUnreadCount();
            userNotificationCountEntity.setUnreadCount(++unreadCount);
        }

        userNotificationCountRepository.save(userNotificationCountEntity);
    }

    @Override
    public void resetUnreadCount(UserEntity principal) {
        Optional<UserNotificationCountEntity> userNotificationCountEntityOpt = userNotificationCountRepository.findById(principal.getUserId());

        UserNotificationCountEntity userNotificationCountEntity;
        if (userNotificationCountEntityOpt.isEmpty()) {
            userNotificationCountEntity = UserNotificationCountEntity.builder()
                    .userId(principal.getUserId())
                    .unreadCount(0)
                    .build();
        } else {
            userNotificationCountEntity = userNotificationCountEntityOpt.get();
            userNotificationCountEntity.setUnreadCount(0);
        }

        userNotificationCountRepository.save(userNotificationCountEntity);
    }
}
