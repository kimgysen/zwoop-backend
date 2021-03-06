package be.zwoop.security;

import be.zwoop.repository.user.UserEntity;
import be.zwoop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Qualifier("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOpt = userRepository.findById(UUID.fromString(username));

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            List<GrantedAuthority> grantedAuthorities = userEntity
                    .getRoles()
                    .stream()
                    .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getRole()))
                    .collect(toList());

            String nickName = userEntity.getNickName() == null
                    ? userEntity.getUserId().toString()
                    : userEntity.getNickName();

            return new UserPrincipal(
                    userEntity.getUserId().toString(),
                    "",
                    grantedAuthorities,
                    nickName,
                    userEntity.getAvatar()
            );

        } else {
            throw new UsernameNotFoundException(
                    format("User: %s, not found", username));
        }
    }
}
