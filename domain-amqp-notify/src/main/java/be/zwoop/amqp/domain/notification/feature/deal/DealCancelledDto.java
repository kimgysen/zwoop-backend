package be.zwoop.amqp.domain.notification.feature.deal;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class DealCancelledDto implements Serializable {
    UUID postId;
    String postTitle;
    UUID askerId;
    UUID respondentId;
}
