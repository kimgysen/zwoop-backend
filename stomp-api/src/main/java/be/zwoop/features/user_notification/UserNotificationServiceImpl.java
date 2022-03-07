package be.zwoop.features.user_notification;

import be.zwoop.domain.user_notification.UserNotificationDto;
import be.zwoop.features.user_notification.repository.cassandra.UserNotificationEntity;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    private final SimpMessagingTemplate wsTemplate;

    private void saveUserNotification(UserNotificationDto userNotificationDto) {
        UserNotificationEntity userNotificationEntity = UserNotificationEntity.builder()

                .build();
    }

    @Override
    public void sendUserNotification(UUID userId, UserNotificationDto userNotificationDto) {
        wsTemplate.convertAndSendToUser(
                userId.toString(),
                notificationDestination(),
                userNotificationDto);
    }

    private String notificationDestination() {
        return "/exchange/amq.direct/notification.received";
    }

}
