package be.zwoop.web.private_chat.dto.send.feature;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PrivateMessageSendDto {

    private final String postId;
    private final String userId;
    private final String partnerId;
    private final Date date;

    private final String fromUserId;
    private final String fromNickName;
    private final String fromAvatar;

    private final String toUserId;
    private final String toNickName;
    private final String toAvatar;

    private final String message;

}
