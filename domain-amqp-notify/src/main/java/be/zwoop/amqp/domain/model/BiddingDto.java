package be.zwoop.amqp.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class BiddingDto implements Serializable {

    private UUID biddingId;
    private UserDto consultant;
    private BigDecimal askPrice;
    private CurrencyDto currencyDto;

}
