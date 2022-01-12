package be.zwoop.repository.redis.chatroom;

import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoomRedisEntity, String> {
}
