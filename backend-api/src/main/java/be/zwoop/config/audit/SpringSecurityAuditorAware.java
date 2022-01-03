package be.zwoop.config.audit;

import be.zwoop.config.security.facade.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNullApi;

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
