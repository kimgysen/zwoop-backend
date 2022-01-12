package be.zwoop.repository.redis.post;

import org.springframework.data.repository.CrudRepository;

public interface PrivateChatRepository extends CrudRepository<PrivateChatRedisEntity, String> {
}
