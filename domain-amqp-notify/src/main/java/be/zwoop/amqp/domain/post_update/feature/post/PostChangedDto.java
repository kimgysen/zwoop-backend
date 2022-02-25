package be.zwoop.amqp.domain.post_update.feature.post;

import be.zwoop.amqp.domain.model.TagDto;
import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class PostChangedDto implements Serializable {
    UserDto op;
    String postTitle;
    String postText;
    BigDecimal bidPrice;
    List<TagDto> tags;
}
