package be.zwoop.web.dto;


import lombok.Data;

@Data
public class PrivateMessageDto {
    String toUserId;
    String toUserNickName;
    String chatRoomId;
    String message;

}
