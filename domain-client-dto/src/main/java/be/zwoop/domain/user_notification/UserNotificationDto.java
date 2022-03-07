package be.zwoop.domain.user_notification;

import be.zwoop.domain.model.user.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
public class UserNotificationDto implements Serializable {
    private UserDto user;
    private UserNotificationType userNotificationType;
    private String notificationText;
    private String metaText;
    private String redirectPath;
    private LocalDateTime notificationDate;
}
