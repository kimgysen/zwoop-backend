package be.zwoop.security.websocket;

import be.zwoop.security.TokenManager;
import be.zwoop.security.UserPrincipal;
import be.zwoop.security.exception.JwtTokenMissingException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;


    public AuthChannelInterceptorAdapter(
            TokenManager tokenManager,
            @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
    }

    @SneakyThrows
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String authorizationHeader = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
            final String jwt = getJwtFromHeader(authorizationHeader);
            String userId = tokenManager.getUsernameFromToken(jwt);
            UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(userId);
            accessor
                    .getSessionAttributes()
                    .put("userPrincipal", principal);

            accessor.setUser(principal::getUsername);
        }
        return message;
    }

    private String getJwtFromHeader(String header) throws JwtTokenMissingException {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }

        return header.substring(7);
    }

}