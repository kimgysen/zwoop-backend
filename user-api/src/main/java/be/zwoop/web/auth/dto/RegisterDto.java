package be.zwoop.web.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {
    @NotNull
    private Integer authProviderId;

    @NotEmpty
    private String authId;

    @NotEmpty
    private String profilePic;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String email;

}
