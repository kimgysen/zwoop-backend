package be.zwoop.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;


public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AccessToken accessToken;
    private final UserDetails userDetails;

    public JwtAuthenticationToken(AccessToken accessToken) {
        super(accessToken.getAuthorities());
        this.accessToken = accessToken;
        this.userDetails = accessToken.getUserDetails();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return accessToken.getJwt();
    }

    @Override
    public Object getDetails() {
        return this.userDetails;
    }
}
