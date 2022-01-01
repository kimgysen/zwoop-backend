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
public class PublicMessagePrimaryKey {

    @PrimaryKeyColumn(name = "chatRoomId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String chatRoomId;

    @PrimaryKeyColumn(name = "date", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date date;


}
