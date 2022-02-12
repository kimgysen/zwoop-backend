package be.zwoop.amqp.domain.post.feature;

import be.zwoop.amqp.domain.common.TagDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class PostRemovedDto implements Serializable {
    UUID postId;
}
