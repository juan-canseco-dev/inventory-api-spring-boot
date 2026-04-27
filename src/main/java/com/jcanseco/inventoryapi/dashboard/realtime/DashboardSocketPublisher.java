package com.jcanseco.inventoryapi.dashboard.realtime;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardSocketPublisher {

    public static final String DASHBOARD_UPDATES_TOPIC = "/topic/dashboard/updates";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishUpdateSignal() {
        var message = DashboardUpdatedMessage.create();
        messagingTemplate.convertAndSend(DASHBOARD_UPDATES_TOPIC, message);
    }
}
