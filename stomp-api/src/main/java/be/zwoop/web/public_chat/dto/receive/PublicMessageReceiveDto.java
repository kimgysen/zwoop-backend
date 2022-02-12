package be.zwoop.web.public_chat.dto.receive;


import lombok.Data;

@Data
public class PublicMessageReceiveDto {
    String chatRoomId;
    String message;
}
