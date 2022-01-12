package be.zwoop.service.message;

import be.zwoop.repository.cassandra.inbox.InboxItemEntity;
import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import be.zwoop.repository.cassandra.public_message.PublicMessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;

public interface MessageService {

    void persistPublicMessage(PublicMessageEntity publicMessageEntity);
    void persistPrivateMessage(PrivateMessageEntity privateMessageEntity);

    List<PublicMessageEntity> findFirst20PublicMessagesByPkChatRoomId(String chatRoomId);
    Slice<PublicMessageEntity> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date);

    List<PrivateMessageEntity> findFirst20PrivateMessagesByPkPostId(String postId, String userId, String partnerId);
    Slice<PrivateMessageEntity> findPrivateMessagesBefore(Pageable pageable, String postId, String userId, String partnerId, Date date);

    List<InboxItemEntity> findAllLastPrivateMessagesByUserId(String postId, String userId);
}
