package be.zwoop.repository.redis.online;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUserRedisEntity, String> {
    boolean existsById(String id);
}
