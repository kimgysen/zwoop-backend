package be.zwoop.security;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


@Getter
public class UserPrincipal extends User {

    private final String nickName;
    private final String avatar;

    public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickName, String avatar) {
        super(username, password, authorities);
        this.nickName = nickName;
        this.avatar = avatar;
    }

}
