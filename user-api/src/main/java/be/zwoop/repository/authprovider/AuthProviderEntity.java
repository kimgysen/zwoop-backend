package be.zwoop.repository.authprovider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "\"AuthProvider\"")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthProviderEntity {

    @Id
    @Column(name = "auth_provider_id")
    int authProviderId;

    @Column(name = "auth_provider")
    String authProvider;

}
