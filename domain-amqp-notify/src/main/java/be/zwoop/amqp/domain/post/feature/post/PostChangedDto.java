package be.zwoop.amqp.domain.post.feature.post;

import be.zwoop.amqp.domain.common.TagDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class PostChangedDto implements Serializable {
    String nickName;
    String postTitle;
    String postText;
    BigDecimal bidPrice;
    List<TagDto> tags;
}
