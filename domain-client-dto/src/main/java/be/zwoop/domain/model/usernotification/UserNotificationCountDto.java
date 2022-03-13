package be.zwoop.domain.model.usernotification;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserNotificationCountDto {
    private int unreadCount;
}
