package be.zwoop.amqp.domain.post;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class PostUpdateFeatureDto<T> implements Serializable {
    PostUpdateType postUpdateType;
    UUID postId;
    T postUpdateDto;
}
