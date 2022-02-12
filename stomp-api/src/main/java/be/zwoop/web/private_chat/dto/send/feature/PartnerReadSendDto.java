package be.zwoop.web.private_chat.dto.send.feature;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PartnerReadSendDto {
    String postId;
    String partnerId;
    Date readDate;
}
