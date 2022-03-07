package be.zwoop.amqp.user_notification;

import be.zwoop.domain.user_notification.UserNotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserNotificationSender {
    private final RabbitTemplate template;
    private final Queue notificationsQueue;

    private final String RABBIT_MESSAGE_SOURCE_HEADER = "backend-api";


    public void sendUserNotification(UserNotificationDto sendDto) {
        template.convertAndSend(
                notificationsQueue.getName(),
                sendDto,
                m -> {
                    m.getMessageProperties()
                            .getHeaders()
                            .put("source", RABBIT_MESSAGE_SOURCE_HEADER);
                    return m;
                });
    }

}
