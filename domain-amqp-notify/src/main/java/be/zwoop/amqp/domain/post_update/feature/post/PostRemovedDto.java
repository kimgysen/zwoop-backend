package be.zwoop.amqp.domain.post_update.feature.post;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class PostRemovedDto implements Serializable {
    UUID postId;
}
