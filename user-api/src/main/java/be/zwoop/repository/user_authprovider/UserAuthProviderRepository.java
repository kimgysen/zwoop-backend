package be.zwoop.repository.user_authprovider;

import be.zwoop.repository.authprovider.AuthProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProviderEntity, UUID> {

    Optional<UserAuthProviderEntity> findByOauthUserIdAndAuthProviderEntity(String oauthUserId, AuthProviderEntity authProviderEntity);

}
