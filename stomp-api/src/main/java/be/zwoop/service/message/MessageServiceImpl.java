package be.zwoop.service.message;


import be.zwoop.repository.cassandra.PrivateMessage;
import be.zwoop.repository.cassandra.PrivateMessageRepository;
import be.zwoop.repository.cassandra.PublicMessage;
import be.zwoop.repository.cassandra.PublicMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final PublicMessageRepository publicMessageRepository;
    private final PrivateMessageRepository privateMessageRepository;

    @Override
    public void persistPublicMessage(PublicMessage publicMessage) {
        publicMessageRepository.save(publicMessage);
    }

    @Override
    public void persistPrivateMessage(PrivateMessage privateMessage) {
        privateMessageRepository.save(privateMessage);
    }

    @Override
    public List<PublicMessage> findFirst20ByPkChatRoomId(String chatRoomId) {
        return publicMessageRepository.findFirst20ByPkChatRoomIdOrderByPkDateDesc(chatRoomId);
    }

    @Override
    public Slice<PublicMessage> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date) {
        return publicMessageRepository.findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(pageable, chatRoomId, date);
    }

    @Override
    public Slice<PrivateMessage> findPrivateMessagesBefore(Pageable pageable, String chatRoomId, String userId, String chatPartnerUserId, Date date) {
        return privateMessageRepository.findAllByPkUserIdEqualsAndFromUserIdEqualsOrToUserIdEqualsAndPkDateGreaterThan(pageable, userId, chatPartnerUserId, chatPartnerUserId, date);
    }
}
