package be.zwoop.web.user.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Data
public class UpdateNickDto {
    @NotEmpty
    @Max(value = 50)
    private String nickName;
}
