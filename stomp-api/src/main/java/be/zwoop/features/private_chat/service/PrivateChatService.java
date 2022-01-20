package be.zwoop.features.private_chat.service;

import be.zwoop.features.private_chat.repository.cassandra.PrivateMessageEntity;
import be.zwoop.features.private_chat.repository.redis.PrivateChatRedisEntity;
import be.zwoop.features.private_chat.repository.redis.PrivateChatUserRedisEntity;
import be.zwoop.web.dto.receive.PrivateMessageReceiveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PrivateChatService {
    PrivateChatRedisEntity getOrCreatePrivateChatRedisEntity(String postId);
    Optional<PrivateChatRedisEntity> findById(String postId);
    void join(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity joiningUser);
    void leave(PrivateChatRedisEntity privateChatRedisEntity, PrivateChatUserRedisEntity leavingUser);
    boolean isUserConnected(String postId, String userId);

    List<PrivateMessageEntity> findFirst20PrivateMessagesByPkPostId(String postId, String userId, String partnerId);
    Slice<PrivateMessageEntity> findPrivateMessagesBefore(Pageable pageable, String postId, String userId, String partnerId, Date date);

    boolean isPartnerWriting(String postId, String userId, String partnerId);
    void startTyping(String postId, String userId, String partnerId);
    void stopTyping(String postId, String userId, String partnerId);
    void stopAllTypingForUser(String postId, String userId);
    void sendPrivateMessage(String postId, String userId, String nickName, String avatar, PrivateMessageReceiveDto msgReceiveDto);

}
