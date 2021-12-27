package be.zwoop.security.facade;

import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * Provide a facade to easily inject the authentication object provided by Spring security
 */
public interface AuthenticationFacade {
    Authentication getAuthentication();
    UUID getAuthenticatedUserId();
}
