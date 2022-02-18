package be.zwoop.features.notification;

import be.zwoop.amqp.domain.notification.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService{

    private final SimpMessagingTemplate wsTemplate;

    @Override
    public void sendNotification(UUID userId, NotificationDto<?> notificationDto) {
        wsTemplate.convertAndSendToUser(
                userId.toString(),
                notificationDestination(),
                notificationDto);
    }

    private String notificationDestination() {
        return "/exchange/amq.direct/notification.received";
    }

}
