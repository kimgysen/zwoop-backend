package be.zwoop.amqp.domain.notification.feature.deal;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class DealOpenedDto implements Serializable {
    UUID postId;
    String postTitle;
    UUID askerId;
    String askerNickName;
    UUID respondentId;
    String respondentNickName;
    BigDecimal dealPrice;
    String currencyCode;
}
