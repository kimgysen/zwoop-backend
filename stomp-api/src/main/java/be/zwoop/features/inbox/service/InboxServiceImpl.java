package be.zwoop.features.inbox.service;

import be.zwoop.features.inbox.factory.InboxItemFactory;
import be.zwoop.features.inbox.repository.cassandra.InboxItemEntity;
import be.zwoop.features.inbox.repository.cassandra.InboxItemRepository;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.web.private_chat.dto.send.feature.PartnerReadSendDto;
import be.zwoop.web.private_chat.dto.send.PrivateChatFeatureDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static be.zwoop.web.private_chat.dto.send.PrivateChatFeatureType.PARTNER_READ;

@AllArgsConstructor
@Service
public class InboxServiceImpl implements InboxService {

    private final InboxItemRepository inboxItemRepository;
    private final InboxItemFactory inboxItemFactory;
    private final SimpMessagingTemplate wsTemplate;

    @Override
    public void persistAndSendInboxItemForUser(PrivateMessageEntity privateMessageEntity, String userId, String partnerId) {
        Optional<InboxItemEntity> inboxItemOpt = findByPostIdAndUserIdAndPartnerId(
                privateMessageEntity.getPk().getPostId(), userId, partnerId);

        InboxItemEntity inboxItemEntity;

        if (inboxItemOpt.isPresent()) {
            InboxItemEntity foundItemEntity = inboxItemOpt.get();
            inboxItemEntity = inboxItemFactory.updateFromPrivateMessage(foundItemEntity, privateMessageEntity);

        } else {
            inboxItemEntity = inboxItemFactory.buildInboxItem(userId, partnerId, privateMessageEntity);
        }

        inboxItemRepository.save(inboxItemEntity);

        wsTemplate.convertAndSendToUser(
                userId,
                inboxItemReceivedDestination(),
                inboxItemEntity);
    }

    @Override
    public List<InboxItemEntity> findAllInboxItemsByPostIdAndUserId(String postId, String userId) {
        return inboxItemRepository.findAllByPkPostIdEqualsAndPkUserIdEquals(postId, userId);
    }

    @Override
    public List<InboxItemEntity> findFirst20InboxItemsByUserId(String userId) {
        return inboxItemRepository.findFirst20ByPkUserIdEquals(userId);
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

    /**
     * This boolean field indicates to the partner that the user has read the message
     */
    @Override
    public void markHasPartnerRead(String postId, String partnerId, String userId) {
        Optional<InboxItemEntity> partnerItemEntityOpt = findByPostIdAndUserIdAndPartnerId(postId, partnerId, userId);

        if (partnerItemEntityOpt.isPresent()) {
            InboxItemEntity partnerItemEntity = partnerItemEntityOpt.get();
            partnerItemEntity.setHasPartnerRead(true);
            inboxItemRepository.save(partnerItemEntity);

            wsTemplate.convertAndSendToUser(
                    partnerId,
                    privateMsgDestination(),
                    PrivateChatFeatureDto.builder()
                            .featureType(PARTNER_READ)
                            .featureDto(
                                PartnerReadSendDto.builder()
                                    .postId(postId)
                                    .partnerId(userId)
                                    .readDate(new Date())
                                    .build())
                            .build()
            );
        }
    }

    @Override
    public Optional<InboxItemEntity> findByPostIdAndUserIdAndPartnerId(String postId, String userId, String partnerId) {
        return inboxItemRepository.findByPkPostIdEqualsAndPkUserIdEqualsAndPkPartnerIdEquals(postId, userId, partnerId);
    }

    private String privateMsgDestination() {
        return "/exchange/amq.direct/private.chat.updates";
    }

    private String inboxItemReceivedDestination() {
        return "/exchange/amq.direct/inbox.item.received";
    }

}
