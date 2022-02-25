package be.zwoop.amqp.domain.common.feature.deal;

import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class DealCancelledDto implements Serializable {
    UUID dealId;
    UserDto op;
    UserDto consultant;
}
