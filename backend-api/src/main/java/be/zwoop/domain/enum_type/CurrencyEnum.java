package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CurrencyEnum {
    BUSD(1);

    @Getter
    private final int value;
}
