package be.zwoop.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByPublicAddressTrx(String publicAddress);
    Optional<UserEntity> findByNickName(String nickName);

    Boolean existsByPublicAddressTrx(String publicAddress);
    Boolean existsByNickName(String nickName);

}
