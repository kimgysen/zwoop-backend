package be.zwoop.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class AccessToken {

    public static final String BEARER = "Bearer ";

    private final TokenManager tokenManager;
    private final String jwt;
    private final UserDetails userDetails;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return tokenManager.getRolesFromToken(jwt);
    }


}
