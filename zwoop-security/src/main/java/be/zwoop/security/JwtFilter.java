package be.zwoop.security;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;

    public JwtFilter(TokenManager tokenManager, @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {

        Optional<String> jwtOpt = getJwtFromHttpOnlyCookie(request, cookieName);

        if (jwtOpt.isPresent()) {
            String jwt = jwtOpt.get();
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(tokenManager.getUsernameFromToken(jwt));
            tokenManager.validateToken(jwt, userDetails);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private Optional<String> getJwtFromHttpOnlyCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays
                    .stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findAny();
        } else {
            return Optional.empty();
        }
    }

}