package be.zwoop.amqp.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Builder
@Data
public class DealDto implements Serializable {

    private UUID dealId;
    private BiddingDto bidding;

}
