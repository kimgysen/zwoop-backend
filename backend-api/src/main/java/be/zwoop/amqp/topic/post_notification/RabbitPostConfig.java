package be.zwoop.amqp.topic.post_notification;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitPostConfig {

    public static final String RABBIT_POST_UPDATES_QUEUE = "rabbit.post.updates.queue-name";
    public static final String RABBIT_POST_UPDATES_DEAD_LETTER_QUEUE = "rabbit.post.updates.dead.letter.queue";
    public static final String RABBIT_POST_UPDATES_DEAD_LETTER_EXCHANGE = "rabbit.post.updates.dead.letter.exchange";


    @Bean
    Queue postUpdatesQueue() {
        final Map<String, Object> queueArguments = new HashMap<>();
        queueArguments.put("x-dead-letter-exchange", RABBIT_POST_UPDATES_DEAD_LETTER_EXCHANGE);
        queueArguments.put("x-dead-letter-routing-key", RABBIT_POST_UPDATES_DEAD_LETTER_QUEUE);

        return QueueBuilder
                .durable(RABBIT_POST_UPDATES_QUEUE)
                .withArguments(queueArguments)
                .build();
    }

    @Bean
    Queue postUpdatesDeadLetterQueue() {
        return QueueBuilder
                .durable(RABBIT_POST_UPDATES_DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    DirectExchange postUpdatesDeadLetterExchange() {
        return new DirectExchange(RABBIT_POST_UPDATES_DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Binding bindPostUpdateDeadLetterQueueWithDeadLetterExchange(
            final Queue postUpdatesDeadLetterQueue,
            final DirectExchange postUpdatesDeadLetterExchange) {
        return BindingBuilder.bind(postUpdatesDeadLetterQueue)
                .to(postUpdatesDeadLetterExchange)
                .with(RABBIT_POST_UPDATES_QUEUE);
    }

}
