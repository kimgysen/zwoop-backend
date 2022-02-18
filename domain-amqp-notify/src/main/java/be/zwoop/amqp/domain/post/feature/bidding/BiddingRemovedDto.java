package be.zwoop.amqp.domain.post.feature.bidding;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class BiddingRemovedDto implements Serializable {
    UUID biddingId;
    UUID userId;
    String nickName;
}
