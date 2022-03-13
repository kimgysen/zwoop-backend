package be.zwoop.domain.model.usernotification;

import be.zwoop.domain.model.user.UserDto;
import be.zwoop.repository.usernotification.UserNotificationEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class UserNotificationDto implements Serializable {
    private final UUID userNotificationId;
    private final String notificationType;
    private final String notificationText;
    private final UserDto sender;
    private final boolean isRead;
    private final String metaInfo;
    private final String redirectParam;
    private final LocalDateTime notificationDate;

    public static UserNotificationDto fromEntity(UserNotificationEntity userNotificationEntity) {
        return UserNotificationDto.builder()
                .sender(UserDto.fromUserEntity(userNotificationEntity.getSender()))
                .notificationType(userNotificationEntity.getNotificationType().getNotificationType())
                .redirectParam(userNotificationEntity.getRedirectParam())
                .metaInfo(userNotificationEntity.getMetaInfo())
                .isRead(userNotificationEntity.isRead())
                .notificationDate(userNotificationEntity.getCreatedAt())
                .build();
    }

    public static Page<UserNotificationDto> fromEntityPage(Page<UserNotificationEntity> userNotificationEntityPage) {
        List<UserNotificationDto> userNotificationList = userNotificationEntityPage
                .stream()
                .map(UserNotificationDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(userNotificationList);
    }
}
