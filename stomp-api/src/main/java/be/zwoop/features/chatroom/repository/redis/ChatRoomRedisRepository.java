package be.zwoop.features.chatroom.repository.redis;

import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRedisRepository extends CrudRepository<ChatRoomRedisEntity, String> {
}
