package be.zwoop.domain.notification.topic.post_update;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
public class PostUpdateDto<T> implements Serializable {
    PostUpdateType postUpdateType;
    UUID postId;
    T dto;
}
