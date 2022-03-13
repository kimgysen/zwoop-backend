package be.zwoop.repository.usernotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "\"UserNotificationCount\"")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationCountEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "unread_count")
    private int unreadCount;

}
