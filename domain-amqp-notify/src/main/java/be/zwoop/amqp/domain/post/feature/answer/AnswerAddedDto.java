package be.zwoop.amqp.domain.post.feature.answer;


import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AnswerAddedDto {
    UUID answerId;
    UserDto consultant;
    String answerText;

}
