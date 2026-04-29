package com.jcanseco.inventoryapi.purchases.usecases.receive;

import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.purchases.domain.PurchaseItem;
import com.jcanseco.inventoryapi.purchases.dto.ReceivePurchaseDto;
import com.jcanseco.inventoryapi.purchases.events.PurchaseReceivedEvent;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import com.jcanseco.inventoryapi.shared.utils.ClockProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.jcanseco.inventoryapi.inventory.stock.persistence.StockSpecifications.byProductIds;

@Service
@RequiredArgsConstructor
public class ReceivePurchaseUseCase {

    private final StockRepository stockRepository;
    private final PurchaseRepository purchaseRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ClockProvider clockProvider;

    @Transactional
    public void execute(ReceivePurchaseDto dto) {
        var purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found.", dto.getPurchaseId())));

        purchase.markAsArrived(dto.getComment(), clockProvider.now());

        var productsWithQuantities = purchase.getItems().stream()
                .collect(Collectors.toMap(PurchaseItem::getProductId, PurchaseItem::getQuantity));

        var productIds = productsWithQuantities.keySet().stream().toList();
        List<Stock> stocks = stockRepository.findAll(byProductIds(productIds));
        stocks.forEach(stock -> stock.addStock(productsWithQuantities.get(stock.getProductId())));

        purchaseRepository.saveAndFlush(purchase);
        stockRepository.saveAllAndFlush(stocks);

        eventPublisher.publishEvent(new PurchaseReceivedEvent(purchase.getId()));
    }
}

