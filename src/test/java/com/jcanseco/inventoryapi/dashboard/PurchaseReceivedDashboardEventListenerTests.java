package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.listeners.PurchaseReceivedDashboardEventListener;
import com.jcanseco.inventoryapi.dashboard.realtime.DashboardSocketPublisher;
import com.jcanseco.inventoryapi.purchases.events.PurchaseReceivedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PurchaseReceivedDashboardEventListenerTests {

    @Mock
    private DashboardSocketPublisher dashboardSocketPublisher;

    @InjectMocks
    private PurchaseReceivedDashboardEventListener listener;

    @Test
    public void handleShouldPublishDashboardUpdateSignal() {
        listener.handle(new PurchaseReceivedEvent(10L));

        verify(dashboardSocketPublisher).publishUpdateSignal();
    }
}
