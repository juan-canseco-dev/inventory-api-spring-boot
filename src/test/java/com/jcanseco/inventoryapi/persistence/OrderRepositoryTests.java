package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.entities.Order;
import com.jcanseco.inventoryapi.repositories.CustomerRepository;
import com.jcanseco.inventoryapi.repositories.OrderRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.specifications.OrderSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Sql("/multiple-orders.sql")
    @Test
    public void createOrderShouldGenerateId() {
        var customer = customerRepository.findById(1L).orElseThrow();
        var productsWithQuantities = new HashMap<Long, Long>() {{
            put(1L, 10L);
            put(2L, 10L);
        }};

        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var order = orderRepository.saveAndFlush(
                Order.createNew(customer, products, productsWithQuantities)
        );

        assertNotNull(order);
        assertTrue(order.getId() > 0);
        assertNotNull(order.getOrderedAt());
        assertFalse(order.isDelivered());
        assertNull(order.getDeliveredAt());

        var foundOrder = orderRepository.findById(order.getId());
        assertTrue(foundOrder.isPresent());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersByOrderedBetweenSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.MAY, 1, 0,0);
        var endDate = LocalDateTime.of(2023, Month.MAY, 28, 0, 0);
        var specification = OrderSpecifications.byOrderedBetween(startDate, endDate);
        var orders = orderRepository.findAll(specification);
        assertNotNull(orders);
        assertEquals(4, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersByDeliveredBetweenSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.JUNE, 5, 0,0);
        var endDate = LocalDateTime.of(2023, Month.JUNE, 19, 0, 0);
        var specification = OrderSpecifications.byDeliveredBetween(startDate, endDate);
        var orders = orderRepository.findAll(specification);
        assertNotNull(orders);
        assertEquals(3, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersByCustomerSpecificationShouldReturnList() {
        var customer = customerRepository.findById(2L).orElseThrow();
        var specification = OrderSpecifications.byCustomer(customer);
        var orders = orderRepository.findAll(specification);
        assertNotNull(orders);
        assertEquals(5, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersByDeliveredSpecificationWhenDeliveredIsTrueShouldReturnList() {
        var deliveredOrders = orderRepository.findAll(
                OrderSpecifications.byDelivered(true)
        );
        assertNotNull(deliveredOrders);
        assertEquals(5, deliveredOrders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersByDeliveredSpecificationWhenDeliveredIsFalseShouldReturnList() {
        var deliveredOrders = orderRepository.findAll(
                OrderSpecifications.byDelivered(false)
        );
        assertNotNull(deliveredOrders);
        assertEquals(5, deliveredOrders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByIdAscFirstOrderIdMustBeOne() {
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByIdAsc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(1L, firstOrder.getId());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByIdDescFirstOrderIdMustBeTen() {
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByIdDesc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(10L, firstOrder.getId());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByTotalAscFirstOrderTotalMustBeExpected() {
        var firstTotal = new BigDecimal("1900.00");
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByTotalAsc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(firstTotal, firstOrder.getTotal());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByTotalDescFirstOrderTotalMustBeExpected() {
        var firstTotal = new BigDecimal("20000.00");
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByTotalDesc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(firstTotal, firstOrder.getTotal());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByCustomerAscFirstOrderCustomerMustBeExpected() {
        var firstCustomerId = 2L;
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByCustomerAsc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(firstCustomerId, firstOrder.getCustomer().getId());
        assertEquals(10, orders.size());
    }


    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByCustomerDescFirstOrderCustomerMustBeExpected() {
        var firstCustomerId = 1L;
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByCustomerDesc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(firstCustomerId, firstOrder.getCustomer().getId());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByOrderedAtAscFirstOrderOrderedAtMustBeExpected() {
        var expectedDate = LocalDateTime.of(2023, Month.MAY, 1, 0,0);
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByOrderedAtAsc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(expectedDate, firstOrder.getOrderedAt());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByOrderedAtDescFirstOrderOrderedAtMustBeExpected() {
        var expectedDate = LocalDateTime.of(2023, Month.JULY, 3, 0,0);
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByOrderedAtDesc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(expectedDate, firstOrder.getOrderedAt());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByDeliveredAtAscFirstOrderDeliveredAtMustBeExpected() {
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByDeliveredAtAsc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertNull(firstOrder.getDeliveredAt());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByDeliveredAtDescFirstOrderDeliveredAtMustBeExpected() {
        var expectedDate = LocalDateTime.of(2023, Month.JULY, 3, 0,0);
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByDeliveredAtDesc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertEquals(expectedDate, firstOrder.getDeliveredAt());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByDeliveredAscFirstOrderMustNotArrived() {
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByDeliveredAsc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertFalse(firstOrder.isDelivered());
        assertEquals(10, orders.size());
    }

    @Test
    @Sql("/multiple-orders.sql")
    public void getOrdersOrderByDeliveredDescFirstOrderMustBeDelivered() {
        var orders = orderRepository.findAll(
                OrderSpecifications.orderByDeliveredDesc(Specification.where(null))
        );
        assertNotNull(orders);
        var firstOrder = orders.get(0);
        assertTrue(firstOrder.isDelivered());
        assertEquals(10, orders.size());
    }

}
