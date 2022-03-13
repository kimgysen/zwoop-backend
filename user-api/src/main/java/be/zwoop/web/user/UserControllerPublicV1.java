package be.zwoop.web.user;

import be.zwoop.domain.model.tag.TagDto;
import be.zwoop.domain.model.user.UserFullDto;
import be.zwoop.repository.authprovider.AuthProviderEntity;
import be.zwoop.repository.authprovider.AuthProviderRepository;
import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.repository.user_authprovider.UserAuthProviderEntity;
import be.zwoop.repository.user_authprovider.UserAuthProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/user")
public class UserControllerPublicV1 {

    private final AuthProviderRepository authProviderRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserFullDto> getUserByoAuthId(
            @RequestParam(value = "oauthId") String oauthId,
            @RequestParam(value = "authProviderId") int authProviderId) {

        Optional<AuthProviderEntity> authProviderEntityOpt = authProviderRepository.findById(authProviderId);

        if (authProviderEntityOpt.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST);

        } else {
            Optional<UserAuthProviderEntity> userAuthProviderEntityOpt =
                    userAuthProviderRepository.findByOauthUserIdAndAuthProviderEntity(oauthId, authProviderEntityOpt.get());

            if (userAuthProviderEntityOpt.isEmpty()) {
                throw new ResponseStatusException(NOT_FOUND);

            } else {
                UserEntity userEntity = userAuthProviderEntityOpt
                        .get()
                        .getUserEntity();
                return ok(UserFullDto.fromEntity(userEntity));
            }
        }
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserFullDto> getUserById(@PathVariable UUID userId) {
        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(userId, false, true);

        if (userEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);
        } else {
            UserEntity userEntity = userEntityOpt.get();
            userEntity.setEmail(null);
            return ok(UserFullDto.fromEntity(userEntity));
        }
    }

    @GetMapping(value = "/{userId}/tags")
    public ResponseEntity<Set<TagDto>> getTags(@PathVariable UUID userId) {
        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(userId, false, true);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();

            return ok(TagDto.fromTagSet(userEntity.getTags()));
        }

        throw new ResponseStatusException(NOT_FOUND);
    }

    @GetMapping(value = "/{userId}/{tag}")
    public ResponseEntity<TagDto> tagExists(
            @PathVariable UUID userId,
            @PathVariable String tag) {
        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(userId, false, true);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            TagEntity foundTagEntity = userEntity
                    .getTags()
                    .stream()
                    .filter(tagEntity -> tagEntity.getTagName().equals(tag))
                    .findFirst()
                    .orElse(null);

            if (foundTagEntity != null) {
                return ok(TagDto.fromEntity(foundTagEntity));
            }
        }

        throw new ResponseStatusException(NOT_FOUND);
    }

}
