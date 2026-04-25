package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.realtime.DashboardSocketPublisher;
import com.jcanseco.inventoryapi.dashboard.realtime.DashboardUpdatedMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DashboardSocketPublisherTests {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private DashboardSocketPublisher dashboardSocketPublisher;

    @Test
    public void publishUpdateSignalShouldSendDashboardUpdateMessageToTopic() {
        dashboardSocketPublisher.publishUpdateSignal();

        verify(messagingTemplate).convertAndSend(
                DashboardSocketPublisher.DASHBOARD_UPDATES_TOPIC,
                DashboardUpdatedMessage.create()
        );
    }
}
