package be.zwoop.service.inbox;

import be.zwoop.repository.cassandra.inbox.InboxItemEntity;
import be.zwoop.repository.cassandra.inbox.InboxItemRepository;
import be.zwoop.repository.cassandra.inbox.factory.InboxItemFactory;
import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
