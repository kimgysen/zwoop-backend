package be.zwoop.amqp.user_notification;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

public class RabbitNotificationConfig {
    public static final String RABBIT_NOTIFICATIONS_QUEUE = "rabbit.notifications.queue-name";
    public static final String RABBIT_NOTIFICATIONS_DEAD_LETTER_QUEUE = "rabbit.notifications.dead.letter.queue";
    public static final String RABBIT_NOTIFICATIONS_DEAD_LETTER_EXCHANGE = "rabbit.notifications.dead.letter.exchange";


    @Bean
    Queue notificationsQueue() {
        final Map<String, Object> queueArguments = new HashMap<>();
        queueArguments.put("x-dead-letter-exchange", RABBIT_NOTIFICATIONS_DEAD_LETTER_EXCHANGE);
        queueArguments.put("x-dead-letter-routing-key", RABBIT_NOTIFICATIONS_DEAD_LETTER_QUEUE);

        return QueueBuilder
                .durable(RABBIT_NOTIFICATIONS_QUEUE)
                .withArguments(queueArguments)
                .build();
    }

    @Bean
    Queue notificationsDeadLetterQueue() {
        return QueueBuilder
                .durable(RABBIT_NOTIFICATIONS_DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    DirectExchange notificationsDeadLetterExchange() {
        return new DirectExchange(RABBIT_NOTIFICATIONS_DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Binding bindNotificationsDeadLetterQueueWithDeadLetterExchange(
            final Queue notificationsDeadLetterQueue,
            final DirectExchange notificationsDeadLetterExchange) {
        return BindingBuilder.bind(notificationsDeadLetterQueue)
                .to(notificationsDeadLetterExchange)
                .with(RABBIT_NOTIFICATIONS_QUEUE);
    }

}
