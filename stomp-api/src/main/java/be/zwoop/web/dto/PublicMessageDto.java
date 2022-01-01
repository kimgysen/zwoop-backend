package be.zwoop.web.dto;


import lombok.Data;

@Data
public class PublicMessageDto {
    String chatRoomId;
    String message;
}
