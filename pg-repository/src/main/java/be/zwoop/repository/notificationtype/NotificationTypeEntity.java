package be.zwoop.repository.notificationtype;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"NotificationType\"")
@Entity
@NoArgsConstructor
@Data
public class NotificationTypeEntity {
    @Id
    @Column(name = "notification_type_id")
    private int notificationTypeId;

    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "description")
    private String description;

}
