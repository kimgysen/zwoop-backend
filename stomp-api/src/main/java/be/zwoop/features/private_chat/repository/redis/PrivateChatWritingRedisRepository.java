package be.zwoop.features.private_chat.repository.redis;

import org.springframework.data.repository.CrudRepository;

public interface PrivateChatWritingRedisRepository extends CrudRepository<PrivateChatWritingRedisEntity, String> {
}
