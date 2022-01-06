package be.zwoop.web.user;

import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.web.user.dto.UpdateAboutDto;
import be.zwoop.web.user.dto.UpdateNickDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/user")
public class UserControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    @PutMapping("/{userId}/nickname")
    public ResponseEntity<UserEntity> updateNickName(@PathVariable String userId, @RequestBody UpdateNickDto nickDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(principalId, false, true);
        validateUser(principalId, userEntityOpt);

        String nickName = nickDto.getNickName();
        Optional<UserEntity> userByNickEntityOpt = userRepository.findByNickName(nickName);

        if (userByNickEntityOpt.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Nickname is already taken.");
        }

        UserEntity userEntity = userEntityOpt.get();
        userEntity.setNickName(nickName);
        userRepository.saveAndFlush(userEntity);

        return ok(userEntity);
    }


    @PutMapping("/{userId}/about")
    public ResponseEntity<UserEntity> updateAboutText(@PathVariable String userId, @RequestBody UpdateAboutDto aboutDto) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(principalId, false, true);
        validateUser(principalId, userEntityOpt);

        UserEntity userEntity = userEntityOpt.get();
        userEntity.setAboutText(aboutDto.getAboutText());
        userRepository.saveAndFlush(userEntity);

        return ok(userEntity);
    }

    private void validateUser(UUID principalId, Optional<UserEntity> userEntityOpt) {
        if (userEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "User not found.");

        } else if (!userEntityOpt.get().getUserId().equals(principalId)) {
            throw new ResponseStatusException(FORBIDDEN, "Forbidden to update nickname.");
        }
    }

}
