package be.zwoop.service.message;


import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PrivateMessageRepository;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.cassandra.PublicMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    private PublicMessageRepository publicMessageRepository;

    private PrivateMessageRepository privateMessageRepository;

    @Override
    public void persistPublicMessage(PublicMessage publicMessage) {
        publicMessageRepository.save(publicMessage);
    }

    @Override
    public void persistPrivateMessage(PrivateMessage privateMessage) {
        privateMessageRepository.save(privateMessage);
    }

    @Override
    public Slice<PublicMessage> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date) {
        return publicMessageRepository.findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(pageable, chatRoomId, date);
    }

    @Override
    public Slice<PrivateMessage> findPrivateMessagesBefore(Pageable pageable, String chatRoomId, String userId, String chatPartnerUserId, Date date) {
        return privateMessageRepository.findAllByPkChatRoomIdEqualsAndPkUserIdEqualsAndFromUserIdEqualsOrToUserIdEqualsAndPkDateGreaterThan(pageable, chatRoomId, userId, chatPartnerUserId, chatPartnerUserId, date);
    }
}
