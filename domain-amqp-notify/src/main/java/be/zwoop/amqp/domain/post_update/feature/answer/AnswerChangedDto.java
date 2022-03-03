package be.zwoop.amqp.domain.post_update.feature.answer;


import be.zwoop.amqp.domain.model.UserDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class AnswerChangedDto implements Serializable {
    UUID answerId;
    UserDto op;
    UserDto consultant;
    String answerText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
