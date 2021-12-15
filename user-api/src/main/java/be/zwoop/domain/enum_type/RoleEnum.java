package be.zwoop.domain.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum RoleEnum {
    USER(1),
    ADMIN(2),
    MODERATOR(3);

    @Getter
    private final int value;

}
