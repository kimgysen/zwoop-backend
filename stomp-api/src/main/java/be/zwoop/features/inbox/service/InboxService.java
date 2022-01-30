package be.zwoop.features.inbox.service;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;

import java.util.List;
import java.util.Optional;

public interface InboxService {
    void persistAndSendInboxItemForUser(PrivateMessageEntity privateLastMessage, String senderId, String senderNickName);
    void markInboxItemAsRead(String postId, String userId, String partnerId);
    void markHasPartnerRead(String postId, String partnerId, String userId);
    List<InboxItemEntity> findAllInboxItemsByPostIdAndUserId(String postId, String userId);
    List<InboxItemEntity> findFirst20InboxItemsByUserId(String userId);
    Optional<InboxItemEntity> findByPostIdAndUserIdAndPartnerId(String postId, String userId, String partnerId);
}
