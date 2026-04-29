package com.jcanseco.inventoryapi.orders.usecases.deliver;

import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.orders.domain.OrderItem;
import com.jcanseco.inventoryapi.orders.dto.DeliverOrderDto;
import com.jcanseco.inventoryapi.orders.events.OrderDeliveredEvent;
import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import java.util.stream.Collectors;
import com.jcanseco.inventoryapi.shared.utils.ClockProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.jcanseco.inventoryapi.inventory.stock.persistence.StockSpecifications.byProductIds;

@Service
@RequiredArgsConstructor
public class DeliverOrderUseCase {

    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final ClockProvider clockProvider;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(DeliverOrderDto dto) {
        var order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new NotFoundException(String.format("Order with the Id : {%d} was not found.", dto.getOrderId())));

        order.deliver(dto.getComment(), clockProvider.now());
        var productsWithQuantities = order.getItems().stream()
                .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

        var stocks = stockRepository.findAll(byProductIds(productsWithQuantities.keySet().stream().toList()));
        stocks.forEach(stock -> stock.removeStock(productsWithQuantities.get(stock.getProductId())));

        orderRepository.saveAndFlush(order);
        stockRepository.saveAllAndFlush(stocks);

        var event = new OrderDeliveredEvent(dto.getOrderId());

        eventPublisher.publishEvent(event);
    }
}

