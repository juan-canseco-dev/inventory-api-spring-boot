package com.jcanseco.inventoryapi.bootstrap.config;

import com.jcanseco.inventoryapi.bootstrap.config.websockets.JwtChannelInterceptor;
import com.jcanseco.inventoryapi.identity.auth.security.DatabaseUserDetailsService;
import com.jcanseco.inventoryapi.identity.auth.security.JwtService;
import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.users.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtChannelInterceptorTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private DatabaseUserDetailsService userDetailsService;

    @InjectMocks
    private JwtChannelInterceptor jwtChannelInterceptor;

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void preSendShouldAuthenticateConnectMessageWhenBearerTokenIsValid() {
        User user = User.builder()
                .email("john@inventory.test")
                .role(Role.builder().permissions(Set.of("inventory.read")).build())
                .build();

        Message<?> message = createConnectMessage("Bearer valid-token");

        when(jwtService.extractUserName("valid-token")).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.isTokenValid("valid-token", user)).thenReturn(true);

        Message<?> interceptedMessage = jwtChannelInterceptor.preSend(message, null);
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(interceptedMessage);

        assertInstanceOf(Authentication.class, accessor.getUser());
        assertEquals(user, ((Authentication) accessor.getUser()).getPrincipal());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    }

    @Test
    public void preSendShouldRejectConnectMessageWhenAuthorizationHeaderIsMissing() {
        Message<?> message = createConnectMessage(null);

        assertThrows(MessageDeliveryException.class, () -> jwtChannelInterceptor.preSend(message, null));
    }

    private Message<?> createConnectMessage(String authorizationHeader) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setLeaveMutable(true);
        if (authorizationHeader != null) {
            accessor.setNativeHeader("Authorization", authorizationHeader);
        }

        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
