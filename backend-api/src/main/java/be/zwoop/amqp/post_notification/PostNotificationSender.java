package be.zwoop.amqp.post_notification;


import be.zwoop.domain.post_update.PostUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostNotificationSender {

    private final RabbitTemplate template;
    private final Queue postUpdatesQueue;

    private final String RABBIT_MESSAGE_SOURCE_HEADER = "backend-api";


    public void sendPostUpdateNotification(PostUpdateDto<?> sendDto) {
        template.convertAndSend(
                postUpdatesQueue.getName(),
                sendDto,
                m -> {
                    m.getMessageProperties()
                            .getHeaders()
                            .put("source", RABBIT_MESSAGE_SOURCE_HEADER);
                    return m;
                });
    }


}
