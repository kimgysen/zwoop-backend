package be.zwoop.repository.usernotification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserNotificationCountRepository extends JpaRepository<UserNotificationCountEntity, UUID> {
}
