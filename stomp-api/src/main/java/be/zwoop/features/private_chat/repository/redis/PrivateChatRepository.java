package be.zwoop.features.private_chat.repository.redis;

import org.springframework.data.repository.CrudRepository;

public interface PrivateChatRepository extends CrudRepository<PrivateChatRedisEntity, String> {
}
