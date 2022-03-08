package be.zwoop.domain.notification.queue.user_notification;

import be.zwoop.domain.model.user.UserDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class UserNotificationDto implements Serializable {
    private final UserDto user;
    private final String notificationType;
    private final String notificationText;
    private final String metaInfo;
    private final String redirectPath;
    private final LocalDateTime notificationDate;
}
