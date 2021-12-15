package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApplicationStatusEnum {
    PENDING(1),
    ACCEPTED(2);

    @Getter
    private final int value;
}
