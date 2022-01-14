package be.zwoop.features.private_chat.repository.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@Table("private_messages")
public class PrivateMessageEntity {

    @PrimaryKey
    private PrivateMessagePrimaryKey pk;

    private String fromUserId;
    private String fromNickName;
    private String fromAvatar;

    private String toUserId;
    private String toNickName;
    private String toAvatar;

    private String message;
}
