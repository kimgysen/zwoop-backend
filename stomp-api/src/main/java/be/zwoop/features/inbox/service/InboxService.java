package be.zwoop.features.inbox.service;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;

import java.util.List;

public interface InboxService {
    void persistInboxItemForUser(PrivateMessageEntity privateLastMessage, String senderId, String senderNickName, boolean isPartnerConnected);
    void markInboxItemAsRead(String postId, String userId, String partnerId);
    List<InboxItemEntity> findAllLastPrivateMessagesByUserId(String postId, String userId);

}
