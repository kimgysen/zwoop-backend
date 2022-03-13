package be.zwoop.web.usernotification;

import be.zwoop.domain.model.usernotification.UserNotificationCountDto;
import be.zwoop.domain.model.usernotification.UserNotificationDto;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import be.zwoop.security.AuthenticationFacade;
import be.zwoop.service.usernotification.db.UserNotificationCountDbService;
import be.zwoop.service.usernotification.db.UserNotificationDbService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/notification")
public class NotificationControllerPrivateV1 {
    private final AuthenticationFacade authenticationFacade;
    private final NotificationControllerValidator validator;
    private final UserNotificationDbService userNotificationDbService;
    private final UserNotificationCountDbService userNotificationCountDbService;


    @GetMapping("/unread/count")
    public ResponseEntity<UserNotificationCountDto> getUnreadCount() {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);
        int unreadCount = userNotificationCountDbService.getUnreadCount(principal);
        return ok(UserNotificationCountDto.builder()
                .unreadCount(unreadCount)
                .build());
    }

    @PutMapping("/unread/reset")
    public ResponseEntity<Void> resetNotificationCount() {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);
        userNotificationCountDbService.resetUnreadCount(principal);
        return ok().build();
    }

    @GetMapping
    public Page<UserNotificationDto> findNotifications(@NotNull final Pageable pageable) {
        UUID principalId = authenticationFacade.getAuthenticatedUserId();
        UserEntity principal = validator.validateAndGetPrincipal(principalId);

        Page<UserNotificationEntity> userNotificationEntityPage = userNotificationDbService.findUserNotifications(pageable, principal);

        return UserNotificationDto
                .fromEntityPage(userNotificationEntityPage);
    }

}
