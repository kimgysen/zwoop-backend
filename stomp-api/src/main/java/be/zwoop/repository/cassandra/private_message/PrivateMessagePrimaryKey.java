package be.zwoop.repository.cassandra.private_message;

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
public class PrivateMessagePrimaryKey {

    @PrimaryKeyColumn(name = "postId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String postId;

    @PrimaryKeyColumn(name = "userId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "partnerId", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private String partnerId;

    @PrimaryKeyColumn(name = "date", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date date;

}
