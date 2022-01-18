package be.zwoop.features.inbox.service;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;

import java.util.List;
import java.util.Optional;

public interface InboxService {
    void persistInboxItemForUser(PrivateMessageEntity privateLastMessage, String senderId, String senderNickName, boolean isPartnerConnected);
    void markInboxItemAsRead(String postId, String userId, String partnerId);
    void markHasPartnerRead(String postId, String partnerId, String userId);
    List<InboxItemEntity> findAllInboxItemsByPostIdAndUserId(String postId, String userId);
    List<InboxItemEntity> findAllInboxItemsByUserId(String userId);
    Optional<InboxItemEntity> findByPostIdAndUserIdAndPartnerId(String postId, String userId, String partnerId);
}
