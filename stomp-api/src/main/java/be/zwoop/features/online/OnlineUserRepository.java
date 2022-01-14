package be.zwoop.features.online;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUserRedisEntity, String> {
    boolean existsById(String id);
}
