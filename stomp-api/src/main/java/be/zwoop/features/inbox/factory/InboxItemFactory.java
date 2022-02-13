package be.zwoop.features.inbox.factory;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.inbox.repository.cassandra.InboxItemPrimaryKey;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InboxItemFactory {

    public InboxItemEntity buildInboxItem(String userId, String partnerId, PrivateMessageEntity msg) {
        InboxItemPrimaryKey pk = InboxItemPrimaryKey
                .builder()
                .postId(msg.getPk().getPostId())
                .userId(userId)
                .partnerId(partnerId)
                .build();

        return InboxItemEntity
                .builder()
                .pk(pk)
                .fromUserId(msg.getFromUserId())
                .fromAvatar(msg.getFromAvatar())
                .fromNickName(msg.getFromNickName())
                .toUserId(msg.getToUserId())
                .toNickName(msg.getToNickName())
                .toAvatar(msg.getToAvatar())
                .lastMessage(msg.getMessage())
                .lastMessageDate(new Date())
                .unread(msg.getFromUserId().equals(userId) ? 0 : 1)
                .build();
    }

    public InboxItemEntity updateFromPrivateMessage(String userId, InboxItemEntity inboxItemEntity, PrivateMessageEntity privateMessageEntity) {
        inboxItemEntity.setFromUserId(privateMessageEntity.getFromUserId());
        inboxItemEntity.setFromNickName(privateMessageEntity.getFromNickName());
        inboxItemEntity.setFromAvatar(privateMessageEntity.getFromAvatar());
        inboxItemEntity.setToUserId(privateMessageEntity.getToUserId());
        inboxItemEntity.setToNickName(privateMessageEntity.getToNickName());
        inboxItemEntity.setToAvatar(privateMessageEntity.getToAvatar());
        inboxItemEntity.setLastMessage(privateMessageEntity.getMessage());
        inboxItemEntity.setLastMessageDate(new Date());
        int unread = inboxItemEntity.getUnread();
        inboxItemEntity.setUnread(
                privateMessageEntity.getFromUserId().equals(userId)
                        ? 0
                        : ++unread);

        return inboxItemEntity;
    }

}
