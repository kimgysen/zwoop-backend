package be.zwoop.web.tag;

import be.zwoop.repository.tag.TagEntity;
import be.zwoop.repository.tag.TagRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.web.tag.dto.IsWatchingDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/tag")
public class TagControllerPrivateV1 {

    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @GetMapping("/{tagName}/watching")
    public ResponseEntity<IsWatchingDto> isWatching(@PathVariable String tagName) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(principalId, false, true);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            Optional<TagEntity> tagEntityOpt = tagRepository.findByTagName(tagName);

            if (tagEntityOpt.isPresent()) {
                TagEntity tagEntity = tagEntityOpt.get();
                Set<TagEntity> userTags = userEntity.getTags();

                boolean isWatching = userTags.contains(tagEntity);

                return ok(
                        IsWatchingDto
                                .builder()
                                .isWatching(isWatching)
                                .tag(tagEntity)
                                .build());
            } else {
                throw new ResponseStatusException(BAD_REQUEST, "Tag does not exist.");
            }
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "User does not exist.");
        }
    }

    @PutMapping("/{tagName}/watch")
    public ResponseEntity<IsWatchingDto> watchTag(@PathVariable String tagName) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(principalId, false, true);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            Optional<TagEntity> tagEntityOpt = tagRepository.findByTagName(tagName);

            if (tagEntityOpt.isPresent()) {
                TagEntity tagEntity = tagEntityOpt.get();
                Set<TagEntity> userTags = userEntity.getTags();

                if (!userTags.contains(tagEntity)) {
                    userTags.add(tagEntity);
                    userEntity.setTags(userTags);
                    userRepository.save(userEntity);

                    return ok(IsWatchingDto
                            .builder()
                            .isWatching(true)
                            .tag(tagEntity)
                            .build());

                } else {
                    throw new ResponseStatusException(CONFLICT, "Tag already watched.");
                }
            } else {
                throw new ResponseStatusException(BAD_REQUEST, "Tag does not exist.");
            }
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "User does not exist.");
        }
    }

    @PutMapping("/{tagName}/unwatch")
    public ResponseEntity<IsWatchingDto> unwatchTag(@PathVariable String tagName) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();

        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndBlockedAndActive(principalId, false, true);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            Optional<TagEntity> tagEntityOpt = tagRepository.findByTagName(tagName);

            if (tagEntityOpt.isPresent()) {
                TagEntity tagEntity = tagEntityOpt.get();
                Set<TagEntity> userTags = userEntity.getTags();

                if (userTags.contains(tagEntity)) {
                    userTags.remove(tagEntity);
                    userEntity.setTags(userTags);
                    userRepository.save(userEntity);

                    return ok(IsWatchingDto
                            .builder()
                            .isWatching(false)
                            .tag(tagEntity)
                            .build());

                } else {
                    throw new ResponseStatusException(CONFLICT, "Tag already watched.");
                }
            } else {
                throw new ResponseStatusException(BAD_REQUEST, "Tag does not exist.");
            }
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "User does not exist.");
        }
    }

}
