package be.zwoop.repository.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@Table("private_messages")
public class PrivateMessage {

    @PrimaryKey
    private MessagePrimaryKey pk;

    private String fromUserId;
    private String fromNickName;

    private String toUserId;
    private String toNickName;

    private String message;
}
