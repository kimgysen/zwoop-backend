package be.zwoop.repository.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Builder
@Data
@Table("public_messages")
public class PublicMessage {

    @PrimaryKey
    private PublicMessagePrimaryKey pk;

    private String fromUserId;
    private String fromUserNickName;
    private String message;

}
