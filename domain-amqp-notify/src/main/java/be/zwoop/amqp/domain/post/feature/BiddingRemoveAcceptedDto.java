package be.zwoop.amqp.domain.post.feature;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class BiddingRemoveAcceptedDto implements Serializable {
    UUID biddingId;
    UUID userId;
    String nickName;
}
