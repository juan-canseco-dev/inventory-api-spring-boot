package com.jcanseco.inventoryapi.dashboard.listeners;

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
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PurchaseReceivedEvent event) {

        log.info("Updating dashboard KPIs after received purchase {}", event.purchaseId());

        // todo: implement later

        // build info here


        // here send the info to the sockets and all the connected clients
    }
}
