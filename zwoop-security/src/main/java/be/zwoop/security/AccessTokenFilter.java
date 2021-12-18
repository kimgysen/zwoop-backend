package be.zwoop.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


@Slf4j
public class AccessTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final String cookieName;
    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;

    public AccessTokenFilter(
            String cookieName,
            TokenManager tokenManager,
            @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager) {
        super(AnyRequestMatcher.INSTANCE);
        this.cookieName = cookieName;
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(request.getRequestURL().toString());

        Optional<String> jwtOpt = getJwtFromHttpOnlyCookie(request, cookieName);

        if (jwtOpt.isPresent()) {
            String jwt = jwtOpt.get();
            UserDetails userDetails = userDetailsService.loadUserByUsername(
                    tokenManager.getUsernameFromToken(jwt));

            AccessToken accessToken = new AccessToken(tokenManager, jwt, userDetails);
            return this.getAuthenticationManager()
                    .authenticate(new JwtAuthenticationToken(accessToken));

        } else {
            throw new BadCredentialsException("Jwt cookie is missing");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);
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
