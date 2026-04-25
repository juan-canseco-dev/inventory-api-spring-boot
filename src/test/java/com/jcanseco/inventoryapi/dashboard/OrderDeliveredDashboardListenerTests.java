package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.dashboard.listeners.OrderDeliveredDashboardListener;
import com.jcanseco.inventoryapi.dashboard.realtime.DashboardSocketPublisher;
import com.jcanseco.inventoryapi.orders.events.OrderDeliveredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderDeliveredDashboardListenerTests {

    @Mock
    private DashboardSocketPublisher dashboardSocketPublisher;

    @InjectMocks
    private OrderDeliveredDashboardListener listener;

    @Test
    public void handleShouldPublishDashboardUpdateSignal() {
        listener.handle(new OrderDeliveredEvent(10L));

        verify(dashboardSocketPublisher).publishUpdateSignal();
    }
}
