package be.zwoop.service.message;

import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PublicMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;

public interface MessageService {

    void persistPublicMessage(PublicMessage publicMessage);
    void persistPrivateMessage(PrivateMessage privateMessage);

    Slice<PublicMessage> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date);
    Slice<PrivateMessage> findPrivateMessagesBefore(Pageable pageable, String chatRoomId, String userId, String chatPartnerUserId, Date date);

}
