package be.zwoop.amqp.domain.post.feature.answer;


import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AnswerRemovedDto {
    UUID answerId;
    UserDto consultant;
}
