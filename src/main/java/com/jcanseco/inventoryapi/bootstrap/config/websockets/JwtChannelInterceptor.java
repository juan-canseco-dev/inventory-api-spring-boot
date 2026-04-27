package com.jcanseco.inventoryapi.bootstrap.config.websockets;

import com.jcanseco.inventoryapi.identity.auth.security.DatabaseUserDetailsService;
import com.jcanseco.inventoryapi.identity.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Profile("!test")
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class JwtChannelInterceptor implements ChannelInterceptor {

    public static final String JWT_HEADER = "Authorization";
    private final JwtService jwtService;
    private final DatabaseUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Objects.requireNonNull(accessor);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            var authHeaderList = accessor.getNativeHeader(JWT_HEADER);
            log.info("Received Authorization header: {}", authHeaderList);

            Objects.requireNonNull(authHeaderList);

            String authHeader = authHeaderList.get(0);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String username = jwtService.extractUserName(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                log.info("User Authorities : {}", token.getAuthorities());
                accessor.setUser(token);
            }
        }
        else {
            log.info("Authorization Header is not present");
        }
        return message;
    }
}
