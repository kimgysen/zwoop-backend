package be.zwoop.domain.model.answer;

import be.zwoop.domain.model.user.UserDto;
import be.zwoop.repository.answer.AnswerEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AnswerDto implements Serializable {
    private final UUID answerId;
    private final UserDto op;
    private final UserDto consultant;
    private final String answerText;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static AnswerDto fromEntity(AnswerEntity answerEntity) {
        UserDto op = UserDto.fromUserEntity(answerEntity.getPost().getOp());
        UserDto consultant = UserDto.fromUserEntity(answerEntity.getConsultant());
        return AnswerDto.builder()
                .answerId(answerEntity.getAnswerId())
                .op(op)
                .consultant(consultant)
                .answerText(answerEntity.getAnswerText())
                .createdAt(answerEntity.getCreatedAt())
                .updatedAt(answerEntity.getUpdatedAt())
                .build();
    }

}
