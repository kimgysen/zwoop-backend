package be.zwoop.web.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull
    private Integer authProviderId;

    @NotEmpty
    private String authId;

}
