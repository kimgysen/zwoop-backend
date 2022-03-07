package be.zwoop.features.user_notification.repository.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.Date;

@Builder
@Data
@PrimaryKeyClass
public class UserNotificationPrimaryKey {

    @PrimaryKeyColumn(name = "userId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "notificationDate", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date notificationDate;

}
