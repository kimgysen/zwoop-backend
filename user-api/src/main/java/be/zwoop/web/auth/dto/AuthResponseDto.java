package be.zwoop.web.auth.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponseDto {
    private final String accessToken;
    private final String userId;
    private final String firstName;
    private final String profilePic;
}
