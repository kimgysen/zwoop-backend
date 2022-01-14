package be.zwoop.features.inbox.service;

import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.inbox.repository.cassandra.InboxItemRepository;
import be.zwoop.features.inbox.factory.InboxItemFactory;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class InboxServiceImpl implements InboxService {

    private final InboxItemRepository inboxItemRepository;
    private final InboxItemFactory inboxItemFactory;

    @Override
    public void persistInboxItemForUser(PrivateMessageEntity privateMessageEntity, String userId, String partnerId, boolean isReceiverConnected) {
        Optional<InboxItemEntity> inboxItemOpt = findByPostIdAndUserIdAndPartnerId(
                privateMessageEntity.getPk().getPostId(), userId, partnerId);

        int unread = 0;
        if (inboxItemOpt.isPresent()) {
            InboxItemEntity inboxItemEntity = inboxItemOpt.get();
            unread = inboxItemEntity.getUnread();
            inboxItemRepository.delete(inboxItemEntity);

        }

        InboxItemEntity inboxItemEntity = inboxItemFactory.buildInboxItem(userId, partnerId, privateMessageEntity);

        if (!isReceiverConnected && privateMessageEntity.getToUserId().equals(userId)) {
            inboxItemEntity.setUnread(++unread);
        }

        inboxItemRepository.save(inboxItemEntity);
    }

    @Override
    public List<InboxItemEntity> findAllLastPrivateMessagesByUserId(String postId, String userId) {
        return inboxItemRepository.findAllByPkPostIdEqualsAndPkUserIdEqualsOrderByPkLastMessageDateDesc(postId, userId);
    }

    @Override
    public void markInboxItemAsRead(String postId, String userId, String partnerId) {
        Optional<InboxItemEntity> inboxItemEntityOpt = findByPostIdAndUserIdAndPartnerId(
                postId, userId, partnerId);

        if (inboxItemEntityOpt.isPresent()) {
            InboxItemEntity inboxItemEntity = inboxItemEntityOpt.get();
            inboxItemEntity.setUnread(0);
            inboxItemRepository.save(inboxItemEntity);
        }

    }

    private Optional<InboxItemEntity> findByPostIdAndUserIdAndPartnerId(String postId, String userId, String partnerId) {
        return inboxItemRepository.findByPkPostIdEqualsAndPkUserIdEquals(postId, userId)
                .stream()
                .filter(entity -> entity.getPartnerId().equals(partnerId))
                .findFirst();
    }

}
