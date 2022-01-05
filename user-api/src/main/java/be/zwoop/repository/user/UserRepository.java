package be.zwoop.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUserIdAndBlockedAndActive(UUID userId, boolean isBlocked, boolean isActive);
    Optional<UserEntity> findByNickNameAndBlockedAndActive(String nickName, boolean isBlocked, boolean isActive);

    Boolean existsByNickName(String nickName);

}
