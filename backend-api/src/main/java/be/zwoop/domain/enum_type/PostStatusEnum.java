package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PostStatusEnum {
    POST_INIT(1),
    DEAL_INIT(2),
    ANSWERED(3),
    ANSWER_ACCEPTED(4),
    PAID(5);

    @Getter
    private final int value;

}
