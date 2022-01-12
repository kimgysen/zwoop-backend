package be.zwoop.service.chatroom;

import be.zwoop.repository.cassandra.public_message.PublicMessageEntity;
import be.zwoop.repository.cassandra.public_message.factory.PublicMessageFactory;
import be.zwoop.repository.redis.chatroom.ChatRoomRedisEntity;
import be.zwoop.repository.redis.chatroom.ChatRoomRepository;
import be.zwoop.repository.redis.chatroom.ChatRoomUserRedisEntity;
import be.zwoop.service.message.MessageService;
import be.zwoop.web.dto.receive.PublicMessageReceiveDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final SimpMessagingTemplate wsTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageService messageService;
    private final PublicMessageFactory publicMessageFactory;


    @Override
    public ChatRoomRedisEntity getOrCreateChatRoomRedisEntity(String chatRoomId) {
        ChatRoomRedisEntity chatRoomRedisEntity;
        Optional<ChatRoomRedisEntity> chatRoomOpt = findById(chatRoomId);

        if (chatRoomOpt.isEmpty()) {
            chatRoomRedisEntity = chatRoomRepository.save(new ChatRoomRedisEntity(chatRoomId, chatRoomId));
        } else {
            chatRoomRedisEntity = chatRoomOpt.get();
        }

        return chatRoomRedisEntity;
    }

    @Override
    public Optional<ChatRoomRedisEntity> findById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    @Override
    public void join(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity joiningUser) {
        chatRoomRedisEntity.addUser(joiningUser);
        chatRoomRepository.save(chatRoomRedisEntity);
        updateConnectedUsersViaWebSocket(chatRoomRedisEntity);
    }

    @Override
    public void leave(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity leavingUser) {
        chatRoomRedisEntity.removeUser(leavingUser);
        chatRoomRepository.save(chatRoomRedisEntity);
        updateConnectedUsersViaWebSocket(chatRoomRedisEntity);
    }

    private void updateConnectedUsersViaWebSocket(ChatRoomRedisEntity chatRoomRedisEntity) {
        wsTemplate.convertAndSend(
                connectedUsersDestination(chatRoomRedisEntity.getId()),
                chatRoomRedisEntity.getConnectedUsers()
        );
    }

    @Override
    public Set<ChatRoomUserRedisEntity> getConnectedUsers(String chatRoomId) {
        Optional<ChatRoomRedisEntity> chatRoomOpt = findById(chatRoomId);

        if (chatRoomOpt.isPresent()) {
            return chatRoomOpt.get()
                    .getConnectedUsers();

        } else {
            return new HashSet<>();
        }
    };

    @Override
    public void sendPublicMessage(String userId, String nickName, String avatar, PublicMessageReceiveDto msgReceiveDto) {
        wsTemplate.convertAndSend(
                publicMsgDestination(msgReceiveDto.getChatRoomId()),
                publicMessageFactory.buildPublicMessageSendDto(userId, nickName, avatar, msgReceiveDto)
        );
        PublicMessageEntity publicMessageEntity = publicMessageFactory.buildPublicMessageEntity(userId, nickName, avatar, msgReceiveDto);
        messageService.persistPublicMessage(publicMessageEntity);
    }


    private String publicMsgDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".public.messages";
    }

    private String connectedUsersDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".connected.users";
    }

}
