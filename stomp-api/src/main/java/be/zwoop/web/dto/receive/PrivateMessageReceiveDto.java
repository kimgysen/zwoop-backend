package be.zwoop.web.dto.receive;


import lombok.Data;

@Data
public class PrivateMessageReceiveDto {
    String postId;
    String toUserId;
    String toUserNickName;
    String toUserAvatar;
    String message;

}
