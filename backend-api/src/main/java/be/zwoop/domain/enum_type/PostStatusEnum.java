package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PostStatusEnum {
    OPEN(1),
    IN_PROGRESS(2),
    CLOSED(3);

    @Getter
    private final int value;

}
