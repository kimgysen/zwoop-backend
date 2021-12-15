package be.zwoop.domain.enum_type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerStatusEnum {
    PENDING(1),
    ACCEPTED(2);

    @Getter
    private final int value;

}
