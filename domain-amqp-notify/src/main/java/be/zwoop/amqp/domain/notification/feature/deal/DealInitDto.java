package be.zwoop.amqp.domain.notification.feature.deal;

import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class DealInitDto implements Serializable {
    UUID dealId;
    UUID postId;
    String postTitle;
    UserDto op;
    UserDto consultant;
    BigDecimal dealPrice;
    String currencyCode;
}
