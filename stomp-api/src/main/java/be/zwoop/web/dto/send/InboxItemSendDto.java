package be.zwoop.web.dto.send;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class InboxItemSendDto {
    private String postId;
    private String userId;
    private String partnerId;

    private String fromUserId;
    private String fromNickName;
    private String fromAvatar;
    private String toUserId;
    private String toNickName;
    private String toAvatar;

    private int unread = 0;
    private boolean hasPartnerRead;
    private Date lastMessageDate;
    private String lastMessage;

}
