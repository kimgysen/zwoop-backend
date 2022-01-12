package be.zwoop.service.private_chat;


import be.zwoop.repository.cassandra.private_message.PrivateMessageEntity;
import be.zwoop.repository.cassandra.private_message.factory.PrivateMessageFactory;
import be.zwoop.repository.redis.chatroom.ChatRoomRedisEntity;
import be.zwoop.repository.redis.online.OnlineUserRepository;
import be.zwoop.repository.redis.post.PrivateChatRedisEntity;
import be.zwoop.repository.redis.post.PrivateChatRepository;
import be.zwoop.repository.redis.post.PrivateChatUserRedisEntity;
import be.zwoop.service.inbox.InboxService;
import be.zwoop.service.message.MessageService;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class PrivateChatServiceImpl implements PrivateChatService{

    private final SimpMessagingTemplate wsTemplate;
    private final PrivateChatRepository privateChatRepository;
    private final MessageService messageService;
    private final InboxService inboxService;
    private final PrivateMessageFactory privateMessageFactory;



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
    public void sendPrivateMessage(String chatRoomId, String userId, String nickName, String avatar, PrivateMessageReceiveDto msgReceiveDto) {
        sendPrivateMessageToSender(chatRoomId, userId, nickName, avatar, msgReceiveDto);
        sendPrivateMessageToReceiver(chatRoomId, userId, nickName, avatar, msgReceiveDto);
    }

    private void sendPrivateMessageToSender(String chatRoomId, String senderId, String senderNickName, String senderAvatar, PrivateMessageReceiveDto msgReceiveDto) {
        wsTemplate.convertAndSendToUser(
                senderId,
                privateMsgDestination(chatRoomId),
                privateMessageFactory.buildPrivateMessageSendDto(senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto)
        );

        PrivateMessageEntity msgEntity = privateMessageFactory.buildPrivateMessage(
                senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto);
        messageService.persistPrivateMessage(msgEntity);
        inboxService.persistInboxItemForUser(msgEntity, senderId, msgReceiveDto.getToUserId(), true);
    }

    private void sendPrivateMessageToReceiver(String chatRoomId, String senderId, String senderNickName, String senderAvatar, PrivateMessageReceiveDto msgReceiveDto) {
        boolean isReceiverConnected = isUserConnected(chatRoomId, msgReceiveDto.getToUserId());

        if (isReceiverConnected) {
            wsTemplate.convertAndSendToUser(
                    msgReceiveDto.getToUserId(),
                    privateMsgDestination(chatRoomId),
                    privateMessageFactory.buildPrivateMessageSendDto(senderId, msgReceiveDto.getToUserId(), senderId, senderNickName, senderAvatar, msgReceiveDto)
            );
        }

        PrivateMessageEntity msgEntity = privateMessageFactory.buildPrivateMessage(
                msgReceiveDto.getToUserId(), senderId, senderId, senderNickName, senderAvatar, msgReceiveDto);
        messageService.persistPrivateMessage(msgEntity);
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

    private String privateMsgDestination(String chatRoomId) {
        return "/exchange/amq.direct/" + chatRoomId + ".private.messages";
    }

}
