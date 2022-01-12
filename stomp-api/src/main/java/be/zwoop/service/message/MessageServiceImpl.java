package be.zwoop.service.message;


import be.zwoop.repository.cassandra.inbox.InboxItemEntity;
import be.zwoop.repository.cassandra.inbox.InboxItemRepository;
import be.zwoop.repository.cassandra.inbox.factory.InboxItemFactory;
import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import be.zwoop.repository.cassandra.private_message.PrivateMessageRepository;
import be.zwoop.repository.cassandra.public_message.PublicMessageEntity;
import be.zwoop.repository.cassandra.public_message.PublicMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final PublicMessageRepository publicMessageRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final InboxItemRepository inboxItemRepository;

    @Override
    public void persistPublicMessage(PublicMessageEntity publicMessageEntity) {
        publicMessageRepository.save(publicMessageEntity);
    }

    @Override
    public void persistPrivateMessage(PrivateMessageEntity privateMessageEntity) {
        privateMessageRepository.save(privateMessageEntity);
    }


    @Override
    public List<PublicMessageEntity> findFirst20PublicMessagesByPkChatRoomId(String chatRoomId) {
        return publicMessageRepository.findFirst20ByPkChatRoomIdOrderByPkDateDesc(chatRoomId);
    }

    @Override
    public Slice<PublicMessageEntity> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date) {
        return publicMessageRepository.findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(pageable, chatRoomId, date);
    }

    @Override
    public List<PrivateMessageEntity> findFirst20PrivateMessagesByPkPostId(String postId, String userId, String partnerId) {
        return privateMessageRepository.findFirst20ByPkPostIdAndPkUserIdEqualsAndPkPartnerIdEqualsOrderByPkDateDesc(postId, userId, partnerId);
    }

    // TODO: incorrect query
    @Override
    public Slice<PrivateMessageEntity> findPrivateMessagesBefore(Pageable pageable, String postId, String userId, String chatPartnerUserId, Date date) {
        return privateMessageRepository.findAllByPkUserIdEqualsAndFromUserIdEqualsOrToUserIdEqualsAndPkDateGreaterThan(pageable, userId, chatPartnerUserId, chatPartnerUserId, date);
    }

    @Override
    public List<InboxItemEntity> findAllLastPrivateMessagesByUserId(String postId, String userId) {
        return inboxItemRepository.findAllByPkPostIdEqualsAndPkUserIdEqualsOrderByPkLastMessageDateDesc(postId, userId);
    }

}
