package be.zwoop.service.message;

import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PublicMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;

public interface MessageService {

    void persistPublicMessage(PublicMessage publicMessage);
    void persistPrivateMessage(PrivateMessage privateMessage);

    List<PublicMessage> findFirst20ByPkChatRoomId(String chatRoomId);
    Slice<PublicMessage> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date);
    Slice<PrivateMessage> findPrivateMessagesBefore(Pageable pageable, String chatRoomId, String userId, String chatPartnerUserId, Date date);

}
