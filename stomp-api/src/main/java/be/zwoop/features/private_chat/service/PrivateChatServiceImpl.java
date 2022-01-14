package be.zwoop.features.private_chat.service;


import be.zwoop.features.inbox.factory.InboxItemFactory;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.features.private_chat.factory.PrivateMessageFactory;
import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageRepository;
import be.zwoop.features.private_chat.repository.redis.PrivateChatRedisEntity;
import be.zwoop.features.private_chat.repository.redis.PrivateChatRepository;
import be.zwoop.features.private_chat.repository.redis.PrivateChatUserRedisEntity;
import be.zwoop.features.inbox.service.InboxService;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PrivateChatServiceImpl implements PrivateChatService{

    private final SimpMessagingTemplate wsTemplate;
    private final PrivateChatRepository privateChatRepository;
    private final InboxService inboxService;
    private final PrivateMessageFactory privateMessageFactory;
    private final PrivateMessageRepository privateMessageRepository;

    @Override
    public PrivateChatRedisEntity getOrCreatePrivateChatRedisEntity(String postId) {
        PrivateChatRedisEntity privateChatRedisEntity;
        Optional<PrivateChatRedisEntity> privateChatRedisEntityOpt = findById(postId);

        if (privateChatRedisEntityOpt.isEmpty()) {
            privateChatRedisEntity = privateChatRepository.save(new PrivateChatRedisEntity(postId, postId));
        } else {
            privateChatRedisEntity = privateChatRedisEntityOpt.get();
        }

        return privateChatRedisEntity;
    }

    @Override
    public Optional<PrivateChatRedisEntity> findById(String postId) {
        return privateChatRepository.findById(postId);
    }

    @Override
    public void join(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity joiningUser) {
        privateChatRedisEntity.addUser(joiningUser);
        privateChatRepository.save(privateChatRedisEntity);
    }

    @Override
    public void leave(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity leavingUser) {
        privateChatRedisEntity.removeUser(leavingUser);
        privateChatRepository.save(privateChatRedisEntity);
    }


    @Override
    public void sendPrivateMessage(String postId, String userId, String nickName, String avatar, PrivateMessageReceiveDto msgReceiveDto) {
        sendPrivateMessageToSender(postId, userId, nickName, avatar, msgReceiveDto);
        sendPrivateMessageToReceiver(postId, userId, nickName, avatar, msgReceiveDto);
    }

    private void sendPrivateMessageToSender(String postId, String senderId, String senderNickName, String senderAvatar, PrivateMessageReceiveDto msgReceiveDto) {
        wsTemplate.convertAndSendToUser(
                senderId,
                privateMsgDestination(),
                privateMessageFactory.buildPrivateMessageSendDto(senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto)
        );

        PrivateMessageEntity msgEntity = privateMessageFactory.buildPrivateMessage(
                senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto);
        privateMessageRepository.save(msgEntity);
        inboxService.persistInboxItemForUser(msgEntity, senderId, msgReceiveDto.getToUserId(), true);
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

    private void sendPrivateMessageToReceiver(String postId, String senderId, String senderNickName, String senderAvatar, PrivateMessageReceiveDto msgReceiveDto) {
        boolean isReceiverConnected = isUserConnected(postId, msgReceiveDto.getToUserId());

        wsTemplate.convertAndSendToUser(
                msgReceiveDto.getToUserId(),
                privateMsgDestination(),
                privateMessageFactory.buildPrivateMessageSendDto(senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto)
        );

        PrivateMessageEntity msgEntity = privateMessageFactory.buildPrivateMessage(
                msgReceiveDto.getToUserId(), senderId, senderId, senderNickName, senderAvatar, msgReceiveDto);
        privateMessageRepository.save(msgEntity);
        inboxService.persistInboxItemForUser(msgEntity, msgReceiveDto.getToUserId(), senderId, isReceiverConnected);
    }

    @Override
    public boolean isUserConnected(String postId, String userId) {
        Optional<PrivateChatRedisEntity> postOpt = findById(postId);

        return postOpt.map(chatRoomRedisEntity ->
                chatRoomRedisEntity
                        .getConnectedUsers()
                        .stream()
                        .anyMatch(u -> u.getUserId().equals(userId)))
                .orElse(false);
    };

    private String privateMsgDestination() {
        return "/exchange/amq.direct/private.messages";
    }

}
