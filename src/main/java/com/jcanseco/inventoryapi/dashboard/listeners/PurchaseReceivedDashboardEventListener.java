package com.jcanseco.inventoryapi.dashboard.listeners;

import com.jcanseco.inventoryapi.dashboard.realtime.DashboardSocketPublisher;
import com.jcanseco.inventoryapi.purchases.events.PurchaseReceivedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseReceivedDashboardEventListener {
    private final DashboardSocketPublisher dashboardSocketPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PurchaseReceivedEvent event) {
        log.info("Publishing dashboard update signal after received purchase {}", event.purchaseId());
        dashboardSocketPublisher.publishUpdateSignal();
    }
}
