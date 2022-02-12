package be.zwoop.amqp.domain.post.feature;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class BiddingChangedDto implements Serializable {
    UUID biddingId;
    UUID userId;
    String nickName;
    BigDecimal askPrice;
}
