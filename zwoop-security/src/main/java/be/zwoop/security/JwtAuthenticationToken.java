package be.zwoop.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AccessToken accessToken;

    public JwtAuthenticationToken(AccessToken accessToken) {
        super(accessToken.getAuthorities());
        this.accessToken = accessToken;
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
        return super.getDetails();
    }
}
