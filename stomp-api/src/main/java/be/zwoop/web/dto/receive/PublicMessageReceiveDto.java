package be.zwoop.web.dto.receive;


import lombok.Data;

@Data
public class PublicMessageReceiveDto {
    String chatRoomId;
    String message;
}
