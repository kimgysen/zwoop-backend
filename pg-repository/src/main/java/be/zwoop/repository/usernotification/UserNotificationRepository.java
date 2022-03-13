package be.zwoop.repository.usernotification;

import be.zwoop.repository.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, UUID> {
    Page<UserNotificationEntity> findAllByReceiver(Pageable pageable, UserEntity receiver);
}
