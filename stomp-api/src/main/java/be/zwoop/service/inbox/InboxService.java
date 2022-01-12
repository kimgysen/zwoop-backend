package be.zwoop.service.inbox;

import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;

public interface InboxService {
    void persistInboxItemForUser(PrivateMessageEntity privateLastMessage, String senderId, String senderNickName, boolean isPartnerConnected);
    void markInboxItemAsRead(String postId, String userId, String partnerId);

}
