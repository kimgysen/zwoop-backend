package be.zwoop.features.inbox.factory;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;

public interface InboxItemFactory {
    InboxItemEntity buildInboxItem(String userId, String partnerId, PrivateMessageEntity msg);
    InboxItemEntity updateFromPrivateMessage(String userId, InboxItemEntity inboxItemEntity, PrivateMessageEntity privateMessageEntity);
}
