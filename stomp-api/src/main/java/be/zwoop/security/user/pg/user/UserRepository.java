package be.zwoop.security.user.pg.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByNickName(String nickName);

    Boolean existsByNickName(String nickName);

}
