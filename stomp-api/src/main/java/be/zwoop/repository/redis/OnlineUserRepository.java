package be.zwoop.repository.redis;

import org.springframework.data.repository.CrudRepository;

public interface OnlineUserRepository extends CrudRepository<OnlineUser, String> {
    boolean existsByUserId(String userId);
}
