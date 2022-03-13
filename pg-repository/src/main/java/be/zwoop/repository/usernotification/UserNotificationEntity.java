package be.zwoop.repository.usernotification;

import be.zwoop.repository.notificationtype.NotificationTypeEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "\"UserNotification\"")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_notification_id")
    private UUID userNotificationId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "redirect_param")
    private String redirectParam;

    @Column(name = "meta_info")
    private String metaInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_type_id")
    private NotificationTypeEntity notificationType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
