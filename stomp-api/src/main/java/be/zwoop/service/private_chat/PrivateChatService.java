package be.zwoop.service.private_chat;

import be.zwoop.repository.redis.post.PrivateChatRedisEntity;
import be.zwoop.repository.redis.post.PrivateChatUserRedisEntity;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;

import java.util.Optional;

public interface PrivateChatService {
    PrivateChatRedisEntity getOrCreatePrivateChatRedisEntity(String postId);
    Optional<PrivateChatRedisEntity> findById(String postId);
    void join(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity joiningUser);
    void leave(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity leavingUser);
    boolean isUserConnected(String postId, String userId);
    void sendPrivateMessage(String postId, String userId, String nickName, String avatar, PrivateMessageReceiveDto msgReceiveDto);



}
