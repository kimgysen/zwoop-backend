package be.zwoop.amqp.domain.notification;

import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class NotificationDto<T> implements Serializable {
    NotificationType notificationType;
    T dto;
}
