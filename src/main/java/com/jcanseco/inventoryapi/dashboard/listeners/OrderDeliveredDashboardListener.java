package com.jcanseco.inventoryapi.dashboard.listeners;

import com.jcanseco.inventoryapi.dashboard.realtime.DashboardSocketPublisher;
import com.jcanseco.inventoryapi.orders.events.OrderDeliveredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDeliveredDashboardListener {
    private final DashboardSocketPublisher dashboardSocketPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderDeliveredEvent event) {
        log.info("Publishing dashboard update signal after delivered order {}", event.orderId());
        dashboardSocketPublisher.publishUpdateSignal();
    }
}
