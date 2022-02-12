package be.zwoop.features.chatroom.service;

import be.zwoop.features.chatroom.repository.cassandra.ChatRoomMessageEntity;
import be.zwoop.features.chatroom.repository.redis.ChatRoomRedisEntity;
import be.zwoop.features.chatroom.repository.redis.ChatRoomUserRedisEntity;
import be.zwoop.web.public_chat.dto.receive.PublicMessageReceiveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomService {
    ChatRoomRedisEntity getOrCreateChatRoomRedisEntity(String chatRoomId);
    Optional<ChatRoomRedisEntity> findById(String chatRoomId);
    void join(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity joiningUser);
    void leave(ChatRoomRedisEntity chatRoomRedisEntity, ChatRoomUserRedisEntity leavingUser);
    Set<ChatRoomUserRedisEntity> getConnectedUsers(String chatRoomId);

    List<ChatRoomMessageEntity> findFirst20PublicMessagesByPkChatRoomId(String chatRoomId);
    Slice<ChatRoomMessageEntity> findPublicMessagesBefore(Pageable pageable, String chatRoomId, Date date);

    void sendPublicMessage(String userId, String nickName, String avatar, PublicMessageReceiveDto msgReceiveDto);
}
