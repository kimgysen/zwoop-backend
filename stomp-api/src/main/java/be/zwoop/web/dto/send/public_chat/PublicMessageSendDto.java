package be.zwoop.web.dto.send.public_chat;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Builder
@Data
public class PublicMessageSendDto {
    private final String chatRoomId;
    private final Date date;
    private final String fromUserId;
    private final String fromUserNickName;
    private final String fromUserAvatar;
    private final String message;

}
