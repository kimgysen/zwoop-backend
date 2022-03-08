package be.zwoop.features.user_notification.repository.cassandra;

import be.zwoop.domain.notification.queue.UserNotificationType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.keyvalue.annotation.KeySpace;

@Builder
@Data
@KeySpace("zwoop_notification")
@Table("user_notifications")
public class UserNotificationEntity {

    @PrimaryKey
    private UserNotificationPrimaryKey pk;

    private UserNotificationType userNotificationType;
    private String notificationText;
    private String metaText;
    private boolean isUnread;
    private String redirectPath;
}
