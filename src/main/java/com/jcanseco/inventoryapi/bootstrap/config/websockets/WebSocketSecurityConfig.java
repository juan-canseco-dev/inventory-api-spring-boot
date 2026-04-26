package com.jcanseco.inventoryapi.bootstrap.config.websockets;

import com.jcanseco.inventoryapi.dashboard.realtime.DashboardSocketPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@EnableWebSocketSecurity
@Configuration
public class WebSocketSecurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(final MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                .simpTypeMatchers(SimpMessageType.CONNECT).permitAll()
                .simpSubscribeDestMatchers(DashboardSocketPublisher.DASHBOARD_UPDATES_TOPIC)
                .permitAll()
                .build();
    }
}