package be.zwoop.web.usernotification;

import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@AllArgsConstructor
public class NotificationControllerValidator {
    private final UserRepository userRepository;

    UserEntity validateAndGetPrincipal(UUID principalId) {
        Optional<UserEntity> userOpt = userRepository.findById(principalId);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }
        return userOpt.get();
    }
}
