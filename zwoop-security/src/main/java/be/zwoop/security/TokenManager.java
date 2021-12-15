package be.zwoop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.nio.charset.StandardCharsets.UTF_8;


@Component
public class TokenManager {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(UTF_8));
    }


    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());

        return generateToken(claims, userDetails.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String username) {
        long expirationTimeLong = Long.parseLong(expirationTime); //in seconds
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key, HS512)
                .compact();
    }

    public void validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        if (!username.equals(userDetails.getUsername())
                && !isTokenExpired(token)) {
            throw new BadCredentialsException("Jwt is invalid because user details don't match");
        };
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token)
                .getSubject();
    }

    public Collection<GrantedAuthority> getRolesFromToken(String token) {
        return (Collection<GrantedAuthority>) getAllClaimsFromToken(token).get("roles");
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

}
