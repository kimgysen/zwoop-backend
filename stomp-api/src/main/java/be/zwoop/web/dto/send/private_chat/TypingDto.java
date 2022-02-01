package be.zwoop.web.dto.send.private_chat;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TypingDto {
    String postId;
    String partnerId;
}
