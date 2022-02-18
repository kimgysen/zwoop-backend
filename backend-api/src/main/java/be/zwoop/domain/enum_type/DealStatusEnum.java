package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DealStatusEnum {

    OPEN(1),
    PENDING(2);

    @Getter
    private final int value;

}
