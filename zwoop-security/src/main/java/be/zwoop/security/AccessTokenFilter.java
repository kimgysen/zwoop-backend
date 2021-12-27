package be.zwoop.security;

import be.zwoop.security.exception.JwtTokenMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class AccessTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;

    public AccessTokenFilter(
            TokenManager tokenManager,
            @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager) {
        super(AnyRequestMatcher.INSTANCE);
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(request.getRequestURL().toString());

        String jwt = null;
        try {
            jwt = getJwtFromHeader(request);
            UserDetails userDetails = userDetailsService.loadUserByUsername(
                    tokenManager.getUsernameFromToken(jwt));

            AccessToken accessToken = new AccessToken(tokenManager, jwt, userDetails);
            return this.getAuthenticationManager()
                    .authenticate(new JwtAuthenticationToken(accessToken));

        } catch (JwtTokenMissingException e) {
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

    private String getJwtFromHeader(HttpServletRequest request) throws JwtTokenMissingException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }

        return header.substring(7);
    }

}
