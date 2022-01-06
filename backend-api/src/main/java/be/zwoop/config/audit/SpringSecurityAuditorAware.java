package be.zwoop.config.audit;

import be.zwoop.security.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
@AllArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {
    private final AuthenticationFacade authenticationFacade;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.of(authenticationFacade
                .getAuthenticatedUserId());
    }
}
