package be.zwoop.features.chatroom.repository.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Builder
@Data
@Table("chatroom_messages")
public class ChatRoomMessageEntity {

    @PrimaryKey
    private ChatRoomMessagePrimaryKey pk;

    private String fromUserId;
    private String fromUserNickName;
    private String fromUserAvatar;
    private String message;

}
