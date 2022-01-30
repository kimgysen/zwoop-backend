package be.zwoop.repository.user_authprovider;

import be.zwoop.repository.authprovider.AuthProviderEntity;
import be.zwoop.repository.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "\"User_AuthProvider\"")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAuthProviderEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_provider_id")
    private UUID userAuthProviderId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auth_provider_id")
    private AuthProviderEntity authProviderEntity;

    @Column(name = "oauth_user_id")
    private String oauthUserId;

}
