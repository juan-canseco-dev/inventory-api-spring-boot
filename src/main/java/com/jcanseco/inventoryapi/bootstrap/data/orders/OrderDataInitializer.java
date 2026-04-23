package com.jcanseco.inventoryapi.bootstrap.data.orders;

import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.orders.persistence.OrderRepository;
import com.jcanseco.inventoryapi.orders.usecases.create.CreateOrderUseCase;
import com.jcanseco.inventoryapi.orders.usecases.deliver.DeliverOrderUseCase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.jcanseco.inventoryapi.inventory.stock.persistence.StockSpecifications.byProductIds;


@Profile("!test")
@Order(10)
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDataInitializer implements ApplicationRunner {

    private static final long MIN_STOCK_THRESHOLD = 10L;
    private static final int NUMBER_OF_ORDERS_PER_CUSTOMER = 5;
    private static final int MAX_PRODUCTS_PER_ORDER = 3;
    private static final long MAX_QUANTITY_PER_PRODUCT_PER_ORDER = 5L;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StockRepository stockRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting order data initialization.");

        long existingOrders = orderRepository.count();
        if (existingOrders > 0) {
            log.info("Skipping order seeding. Found {} existing orders.", existingOrders);
            return;
        }

        var customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            log.info("Skipping order seeding. No customers found.");
            return;
        }

        var products = productRepository.findAll();
        if (products.isEmpty()) {
            log.info("Skipping order seeding. No products found.");
            return;
        }

        var productsWithAvailableStock = createProductsWithStockMap();
        if (!hasAvailableStock(productsWithAvailableStock)) {
            log.info("Skipping order seeding. No products with stock above threshold {}.", MIN_STOCK_THRESHOLD);
            return;
        }

        var random = ThreadLocalRandom.current();
        int totalOrdersCreated = 0;

        for (var customer : customers) {
            int customerOrdersCreated = 0;

            log.info("Generating up to {} orders for customer id {}.", NUMBER_OF_ORDERS_PER_CUSTOMER, customer.getId());

            while (customerOrdersCreated < NUMBER_OF_ORDERS_PER_CUSTOMER
                    && hasAvailableStock(productsWithAvailableStock)) {

                var orderProducts = createOrderProductsMap(products, productsWithAvailableStock, random);

                if (orderProducts.isEmpty()) {
                    log.warn(
                            "Could not generate more order lines for customer id {} because no valid stock combination was found.",
                            customer.getId()
                    );
                    break;
                }

                Long orderId = createOrderAndMarkAsDelivered(customer.getId(), orderProducts);
                updateAvailableStock(productsWithAvailableStock, orderProducts);


                customerOrdersCreated++;
                totalOrdersCreated++;

                log.debug(
                        "Created order id {} for customer id {} with {} products.",
                        orderId,
                        customer.getId(),
                        orderProducts.size()
                );
            }

            log.info(
                    "Finished generating orders for customer id {}. Total created for customer: {}.",
                    customer.getId(),
                    customerOrdersCreated
            );
        }

        log.info("Order data initialization completed successfully. Total orders created: {}.", totalOrdersCreated);
    }

    private Long createOrderAndMarkAsDelivered(Long customerId, HashMap<Long, Long> productsWithQuantities)
    {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new DomainException(String.format("Customer with the Id : {%d} was not found.", customerId)));

        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var order = com.jcanseco.inventoryapi.orders.domain.Order.createNew(customer, products, productsWithQuantities);
        order.deliver("Delivered");
        var savedOrder = orderRepository.saveAndFlush(order);

        var stocks = stockRepository.findAll(byProductIds(productsWithQuantities.keySet().stream().toList()));
        stocks.forEach(stock -> stock.removeStock(productsWithQuantities.get(stock.getProductId())));

        stockRepository.saveAllAndFlush(stocks);

        return savedOrder.getId();
    }

    private HashMap<Long, Long> createProductsWithStockMap() {
        return stockRepository.findAll()
                .stream()
                .filter(stock -> stock.getProductId() != null)
                .collect(Collectors.toMap(
                        Stock::getProductId,
                        Stock::getQuantity,
                        Long::sum,
                        HashMap::new
                ));
    }

    private HashMap<Long, Long> createOrderProductsMap(
            List<Product> products,
            HashMap<Long, Long> productsWithQuantities,
            ThreadLocalRandom random
    ) {
        var availableProducts = products.stream()
                .filter(product -> {
                    Long currentStock = productsWithQuantities.get(product.getId());
                    return currentStock != null && currentStock > MIN_STOCK_THRESHOLD;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        if (availableProducts.isEmpty()) {
            return new HashMap<>();
        }

        Collections.shuffle(availableProducts);

        int numberOfProductsInOrder = Math.min(
                random.nextInt(1, MAX_PRODUCTS_PER_ORDER + 1),
                availableProducts.size()
        );

        HashMap<Long, Long> orderProducts = new HashMap<>();

        for (int i = 0; i < numberOfProductsInOrder; i++) {
            var product = availableProducts.get(i);
            Long productId = product.getId();
            Long currentStock = productsWithQuantities.get(productId);

            if (currentStock == null || currentStock <= MIN_STOCK_THRESHOLD) {
                continue;
            }

            long maxRequestQuantity = Math.min(
                    MAX_QUANTITY_PER_PRODUCT_PER_ORDER,
                    currentStock - MIN_STOCK_THRESHOLD
            );

            long requestQuantity = random.nextLong(1, maxRequestQuantity + 1);

            if (productHasAvailableStock(productsWithQuantities, productId, requestQuantity)) {
                orderProducts.put(productId, requestQuantity);
            }
        }

        return orderProducts;
    }

    private void updateAvailableStock(
            HashMap<Long, Long> productsWithQuantities,
            HashMap<Long, Long> orderedProducts
    ) {
        orderedProducts.forEach((productId, orderedQuantity) -> {
            Long currentStock = productsWithQuantities.get(productId);
            if (currentStock == null) {
                return;
            }

            long updatedStock = currentStock - orderedQuantity;
            productsWithQuantities.put(productId, updatedStock);
        });
    }

    private boolean productHasAvailableStock(
            HashMap<Long, Long> productsWithQuantities,
            Long productId,
            Long requestQuantity
    ) {
        Long currentStock = productsWithQuantities.get(productId);
        return currentStock != null && (currentStock - requestQuantity) >= MIN_STOCK_THRESHOLD;
    }

    private boolean hasAvailableStock(HashMap<Long, Long> productsWithQuantities) {
        return productsWithQuantities.values()
                .stream()
                .anyMatch(quantity -> quantity != null && quantity > MIN_STOCK_THRESHOLD);
    }
}





