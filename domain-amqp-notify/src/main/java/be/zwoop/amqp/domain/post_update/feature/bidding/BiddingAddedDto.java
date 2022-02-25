package be.zwoop.amqp.domain.post_update.feature.bidding;

import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class BiddingAddedDto implements Serializable {
    UUID biddingId;
    UserDto consultant;
    BigDecimal askPrice;
    String currencyCode;
}