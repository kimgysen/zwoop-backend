package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BiddingStatusEnum {
    PENDING(1),
    ACCEPTED(2);

    @Getter
    private final int value;
}
