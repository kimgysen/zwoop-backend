package be.zwoop.features.user_notification.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;

public interface UserNotificationRepository extends CassandraRepository<UserNotificationEntity, UserNotificationPrimaryKey> {

    List<UserNotificationEntity> findFirst20ByPkUserIdOrderByPkNotificationDateDesc(String userId);
    Slice<UserNotificationEntity> findAllByPkUserIdEqualsAndPkNotificationDateGreaterThan(Pageable pageable, String userId, Date date);

}
