package be.zwoop.features.chatroom.service;

import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageEntity;
import be.zwoop.features.chatroom.factory.ChatRoomMessageFactory;
import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageRepository;
import be.zwoop.features.chatroom.repository.redis.ChatRoomRedisEntity;
import be.zwoop.features.chatroom.repository.redis.ChatRoomRedisRepository;
import be.zwoop.features.chatroom.repository.redis.ChatRoomUserRedisEntity;
import be.zwoop.web.dto.receive.PublicMessageReceiveDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final SimpMessagingTemplate wsTemplate;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomMessageFactory chatRoomMessageFactory;
    private final ChatRoomMessageRepository chatRoomMessageRepository;


    @Override
    public ChatRoomRedisEntity getOrCreateChatRoomRedisEntity(String chatRoomId) {
        ChatRoomRedisEntity chatRoomRedisEntity;
        Optional<ChatRoomRedisEntity> chatRoomOpt = findById(chatRoomId);

        if (chatRoomOpt.isEmpty()) {
            chatRoomRedisEntity = chatRoomRedisRepository.save(new ChatRoomRedisEntity(chatRoomId, chatRoomId));
        } else {
            chatRoomRedisEntity = chatRoomOpt.get();
        }

        return chatRoomRedisEntity;
    }

    @Override
    public Optional<ChatRoomRedisEntity> findById(String chatRoomId) {
        return chatRoomRedisRepository.findById(chatRoomId);
    }

    @Override
    public void join(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity joiningUser) {
        chatRoomRedisEntity.addUser(joiningUser);
        chatRoomRedisRepository.save(chatRoomRedisEntity);
        updateConnectedUsersViaWebSocket(chatRoomRedisEntity);
    }

    @Override
    public void leave(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity leavingUser) {
        chatRoomRedisEntity.removeUser(leavingUser);
        chatRoomRedisRepository.save(chatRoomRedisEntity);
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
    public List<ChatRoomMessageEntity> findFirst20PublicMessagesByPkChatRoomId(String chatRoomId) {
        return chatRoomMessageRepository.findFirst20ByPkChatRoomIdOrderByPkDateDesc(chatRoomId);
    }

    @Override
    public Slice<ChatRoomMessageEntity> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date) {
        return chatRoomMessageRepository.findAllByPkChatRoomIdEqualsAndPkDateGreaterThan(pageable, chatRoomId, date);
    }

    @Override
    public void sendPublicMessage(String userId, String nickName, String avatar, PublicMessageReceiveDto msgReceiveDto) {
        wsTemplate.convertAndSend(
                publicMsgDestination(msgReceiveDto.getChatRoomId()),
                chatRoomMessageFactory.buildPublicMessageSendDto(userId, nickName, avatar, msgReceiveDto)
        );
        ChatRoomMessageEntity chatRoomMessageEntity = chatRoomMessageFactory.buildPublicMessageEntity(userId, nickName, avatar, msgReceiveDto);
        chatRoomMessageRepository.save(chatRoomMessageEntity);
    }


    private String publicMsgDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".chatroom.messages";
    }

    private String connectedUsersDestination(String chatRoomId) {
        return "/topic/" + chatRoomId + ".connected.users";
    }

}
