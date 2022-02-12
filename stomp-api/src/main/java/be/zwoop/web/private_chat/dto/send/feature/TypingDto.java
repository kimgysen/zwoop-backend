package be.zwoop.web.private_chat.dto.send.feature;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TypingDto {
    String postId;
    String partnerId;
}
