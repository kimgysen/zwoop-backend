package be.zwoop.repository.cassandra.inbox.factory;

import be.zwoop.repository.cassandra.inbox.InboxItemEntity;
import be.zwoop.repository.cassandra.inbox.InboxItemPrimaryKey;
import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InboxItemFactory {

    public InboxItemEntity buildInboxItem(String userId, String partnerId, PrivateMessageEntity msg) {
        InboxItemPrimaryKey pk = InboxItemPrimaryKey
                .builder()
                .postId(msg.getPk().getPostId())
                .userId(userId)
                .lastMessageDate(new Date())
                .build();

        return InboxItemEntity
                .builder()
                .pk(pk)
                .partnerId(partnerId)
                .fromUserId(msg.getFromUserId())
                .fromAvatar(msg.getFromAvatar())
                .fromNickName(msg.getFromNickName())
                .toUserId(msg.getToUserId())
                .toNickName(msg.getToNickName())
                .toAvatar(msg.getToAvatar())
                .lastMessage(msg.getMessage())
                .build();
    }
}
