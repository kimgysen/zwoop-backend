package be.zwoop.websocket.service.connect;

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

import java.util.Date;

@AllArgsConstructor
@Service
public class ConnectServiceImpl implements ConnectService {

    private final OnlineUserRepository onlineUserRepository;
    private final ChatRoomService chatRoomService;
    private final PrivateChatService privateChatService;


    public void savePresenceStatusPublicChatRoom(String chatRoomId, UserPrincipal principal) {
        ChatRoomRedisEntity chatRoomRedisEntity = chatRoomService.getOrCreateChatRoomRedisEntity(chatRoomId);
        joinChatRoom(chatRoomRedisEntity, principal);
    }

    public void savePresenceStatusPrivateChat(String postId, UserPrincipal principal) {
        PrivateChatRedisEntity privateChatRedisEntity = privateChatService.getOrCreatePrivateChatRedisEntity(postId);
        joinPrivateChat(privateChatRedisEntity, principal);
    }

    public void saveOnlineStatusRedis(UserPrincipal userPrincipal) {
        onlineUserRepository.save(new OnlineUserRedisEntity(userPrincipal.getUsername()));
    }

    private void joinChatRoom(ChatRoomRedisEntity chatRoomRedisEntity, UserPrincipal principal) {
        ChatRoomUserRedisEntity chatRoomUserRedisEntity = ChatRoomUserRedisEntity.builder()
                .userId(principal.getUsername())
                .nickName(principal.getNickName())
                .avatar(principal.getAvatar())
                .joinedAt(new Date())
                .build();
        chatRoomService.join(chatRoomRedisEntity, chatRoomUserRedisEntity);
    }

    private void joinPrivateChat(PrivateChatRedisEntity privateChatRedisEntity, UserPrincipal principal) {
        PrivateChatUserRedisEntity privateChatUserRedisEntity = PrivateChatUserRedisEntity.builder()
                .userId(principal.getUsername())
                .nickName(principal.getNickName())
                .avatar(principal.getAvatar())
                .joinedAt(new Date())
                .build();
        privateChatService.join(privateChatRedisEntity, privateChatUserRedisEntity);
    }


    @Override
    public boolean isConnectedToChatRoom(String chatRoomId) {
        return false;
    }

    @Override
    public boolean isConnectedToPrivateChat(String postId) {
        return false;
    }

}
