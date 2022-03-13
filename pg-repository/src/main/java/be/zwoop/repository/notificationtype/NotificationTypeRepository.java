package be.zwoop.repository.notificationtype;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationTypeEntity, Integer> {
    NotificationTypeEntity findByNotificationTypeId(int notificationTypeId);
}
