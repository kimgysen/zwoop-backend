package be.zwoop.amqp.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TagDto implements Serializable {
    long tagId;
    String tagName;
}
