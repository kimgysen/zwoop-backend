package be.zwoop.repository.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("public_messages")
public class PublicMessage {

    @PrimaryKey
    private MessagePrimaryKey pk;

    private String fromNickName;
    private String message;

}
