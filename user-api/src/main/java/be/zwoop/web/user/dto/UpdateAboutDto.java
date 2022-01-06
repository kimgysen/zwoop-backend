package be.zwoop.web.user.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;


@Data
public class UpdateAboutDto {
    @NotEmpty
    @Max(value = 500)
    String aboutText;
}
