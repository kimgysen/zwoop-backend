package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DealStatusEnum {

    OPEN(1),
    CANCELLED(2),
    PAID(3);

    @Getter
    private final int value;

}
