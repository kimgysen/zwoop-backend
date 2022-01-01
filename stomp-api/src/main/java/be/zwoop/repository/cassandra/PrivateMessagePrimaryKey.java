package be.zwoop.repository.cassandra;

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

    @PrimaryKeyColumn(name = "userId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "chatPartnerId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String chatPartnerId;

    @PrimaryKeyColumn(name = "date", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date date;


}
