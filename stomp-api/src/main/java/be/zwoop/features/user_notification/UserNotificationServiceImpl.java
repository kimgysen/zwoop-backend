package be.zwoop.features.user_notification;

import be.zwoop.domain.notification.queue.NotificationDto;
import be.zwoop.domain.notification.queue.user_notification.UserNotificationDto;
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
    public void sendUserNotification(UUID userId, NotificationDto<?> notificationDto) {
        wsTemplate.convertAndSendToUser(
                userId.toString(),
                notificationDestination(),
                notificationDto);
    }

    private String notificationDestination() {
        return "/exchange/amq.direct/notification.received";
    }

}
