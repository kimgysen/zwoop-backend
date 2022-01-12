package be.zwoop.repository.cassandra.public_message;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Builder
@Data
@Table("public_messages")
public class PublicMessageEntity {

    @PrimaryKey
    private PublicMessagePrimaryKey pk;

    private String fromUserId;
    private String fromUserNickName;
    private String fromUserAvatar;
    private String message;

}
