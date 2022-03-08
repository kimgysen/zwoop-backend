package be.zwoop.domain.notification.queue;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Getter
@SuperBuilder
public class NotificationDto<T> implements Serializable {
    private final UUID userId;
    private final UserNotificationType userNotificationType;
    T dto;
}
