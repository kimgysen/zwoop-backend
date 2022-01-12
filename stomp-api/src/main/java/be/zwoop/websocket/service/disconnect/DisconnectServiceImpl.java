package be.zwoop.websocket.service.disconnect;

import be.zwoop.repository.redis.chatroom.ChatRoomRedisEntity;
import be.zwoop.repository.redis.chatroom.ChatRoomUserRedisEntity;
import be.zwoop.repository.redis.online.OnlineUserRedisEntity;
import be.zwoop.repository.redis.online.OnlineUserRepository;
import be.zwoop.repository.redis.post.PrivateChatRedisEntity;
import be.zwoop.repository.redis.post.PrivateChatUserRedisEntity;
import be.zwoop.security.UserPrincipal;
import be.zwoop.service.chatroom.ChatRoomService;
import be.zwoop.service.private_chat.PrivateChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class DisconnectServiceImpl implements DisconnectService {

    private final OnlineUserRepository onlineUserRepository;
    private final ChatRoomService chatRoomService;
    private final PrivateChatService privateChatService;


    @Override
    public void saveOfflineStatusRedis(UserPrincipal principal) {
        Optional<OnlineUserRedisEntity> onlineUser = onlineUserRepository.findById(principal.getUsername());
        onlineUser.ifPresent(onlineUserRepository::delete);
    }

    @Override
    public void saveAbsenceStatusPublicChatRoom(String chatRoomId, UserPrincipal principal) {
        Optional<ChatRoomRedisEntity> chatRoomRedisEntityOpt = chatRoomService.findById(chatRoomId);
        chatRoomRedisEntityOpt.ifPresent(
                        chatRoomRedisEntity -> leaveChatRoom(chatRoomRedisEntity, principal));
    }

    @Override
    public void saveAbsenceStatusPrivateChat(String postId, UserPrincipal principal) {
        Optional<PrivateChatRedisEntity> privateChatRedisEntityOpt = privateChatService.findById(postId);
        privateChatRedisEntityOpt.ifPresent(
                privateChatRedisEntity -> leavePrivateChat(privateChatRedisEntity, principal));
    }

    private void leaveChatRoom(ChatRoomRedisEntity chatRoomRedisEntity, UserPrincipal principal) {
        ChatRoomUserRedisEntity chatRoomUserRedisEntity = ChatRoomUserRedisEntity.builder()
                .userId(principal.getUsername())
                .build();
        chatRoomService.leave(chatRoomRedisEntity, chatRoomUserRedisEntity);
    }

    private void leavePrivateChat(PrivateChatRedisEntity privateChatRedisEntity, UserPrincipal principal) {
        PrivateChatUserRedisEntity privateChatUserRedisEntity = PrivateChatUserRedisEntity.builder()
                .userId(principal.getUsername())
                .build();
        privateChatService.leave(privateChatRedisEntity, privateChatUserRedisEntity);
    }


}