package be.zwoop.web.dto.send;

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
