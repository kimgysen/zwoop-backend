package be.zwoop.web.auth;


import be.zwoop.domain.enum_type.RoleEnum;
import be.zwoop.repository.authprovider.AuthProviderEntity;
import be.zwoop.repository.authprovider.AuthProviderRepository;
import be.zwoop.repository.role.RoleEntity;
import be.zwoop.repository.role.RoleRepository;
import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import be.zwoop.repository.user_authprovider.UserAuthProviderEntity;
import be.zwoop.repository.user_authprovider.UserAuthProviderRepository;
import be.zwoop.security.TokenManager;
import be.zwoop.web.auth.dto.AuthResponseDto;
import be.zwoop.web.auth.dto.LoginDto;
import be.zwoop.web.auth.dto.RegisterDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/public/auth")
public class AuthControllerV1 {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService;
    private final TokenManager tokenManager;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        Optional<AuthProviderEntity> authProviderEntityOpt = authProviderRepository.findById(loginDto.getAuthProviderId());

        if (authProviderEntityOpt.isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, "Given Auth provider is not valid");

        Optional<UserAuthProviderEntity> userAuthProviderEntityOpt =
                userAuthProviderRepository
                        .findByOauthUserIdAndAuthProviderEntity(loginDto.getAuthId(), authProviderEntityOpt.get());

        if (userAuthProviderEntityOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND);

        } else {
            UserEntity userEntity = userAuthProviderEntityOpt.get().getUserEntity();

            final UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getUserId().toString());
            String genJwt = tokenManager.createToken(userDetails);
            AuthResponseDto respDto = AuthResponseDto.builder()
                    .accessToken(genJwt)
                    .userId(userEntity.getUserId().toString())
                    .firstName(userEntity.getFirstName())
                    .profilePic(userEntity.getProfilePic())
                    .build();

            return ok(respDto);
        }
    }

    @Transactional
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            Optional<AuthProviderEntity> authProviderEntityOpt = authProviderRepository.findById(registerDto.getAuthProviderId());

            if (authProviderEntityOpt.isEmpty())
                throw new ResponseStatusException(BAD_REQUEST, "Given Auth provider is not valid");

            Optional<UserAuthProviderEntity> userAuthProviderEntityOpt =
                    userAuthProviderRepository
                            .findByOauthUserIdAndAuthProviderEntity(registerDto.getAuthId(), authProviderEntityOpt.get());

            UserEntity savedUser;
            if (userAuthProviderEntityOpt.isEmpty()) {
                RoleEntity roleEntity = roleRepository.getById(RoleEnum.USER.getValue());
                UserEntity toSave = UserEntity.builder()
                        .firstName(registerDto.getFirstName())
                        .lastName(registerDto.getLastName())
                        .profilePic(registerDto.getProfilePic())
                        .email(registerDto.getEmail())
                        .roles(Set.of(roleEntity))
                        .isActive(true)
                        .build();
                savedUser = userRepository.saveAndFlush(toSave);

                UserAuthProviderEntity userAuthProviderEntity = UserAuthProviderEntity
                        .builder()
                        .authProviderEntity(authProviderEntityOpt.get())
                        .oauthUserId(registerDto.getAuthId())
                        .userEntity(savedUser)
                        .build();
                userAuthProviderRepository.saveAndFlush(userAuthProviderEntity);

            } else {
                savedUser = userAuthProviderEntityOpt.get().getUserEntity();
            }

            // Generate jwt to use
            final UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUserId().toString());
            String genJwt = tokenManager.createToken(userDetails);
            AuthResponseDto respDto = AuthResponseDto.builder()
                    .accessToken(genJwt)
                    .userId(savedUser.getUserId().toString())
                    .firstName(savedUser.getFirstName())
                    .profilePic(savedUser.getProfilePic())
                    .build();

            return ok(respDto);

        } catch (Exception e) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }

    }

//   TODO: Implement security against unauthorized registration
//
//    private String getJwtFromHeaders(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeaderIsInvalid(authorizationHeader)) {
//            throw new ResponseStatusException(UNAUTHORIZED);
//        }
//
//        return authorizationHeader.replace("Bearer ", "");
//    }
//
//    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
//        return authorizationHeader == null
//                || !authorizationHeader.startsWith("Bearer ");
//    }


}
