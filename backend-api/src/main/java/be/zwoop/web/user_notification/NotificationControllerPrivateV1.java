package be.zwoop.web.user_notification;

import be.zwoop.security.AuthenticationFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/private/notification")
public class NotificationControllerPrivateV1 {
    private final AuthenticationFacade authenticationFacade;


}
