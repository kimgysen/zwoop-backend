package be.zwoop.service.chatroom;

import be.zwoop.repository.redis.chatroom.ChatRoomRedisEntity;
import be.zwoop.repository.redis.chatroom.ChatRoomUserRedisEntity;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
import be.zwoop.web.dto.receive.PublicMessageReceiveDto;

import java.util.Optional;
import java.util.Set;

public interface ChatRoomService {
    ChatRoomRedisEntity getOrCreateChatRoomRedisEntity(String chatRoomId);
    Optional<ChatRoomRedisEntity> findById(String chatRoomId);
    void join(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity joiningUser);
    void leave(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity leavingUser);
    Set<ChatRoomUserRedisEntity> getConnectedUsers(String chatRoomId);
    void sendPublicMessage(String userId, String nickName, String avatar, PublicMessageReceiveDto msgReceiveDto);
}
