package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.orders.OrderItemDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTests {
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
    private Supplier supplier;
    private Customer customer;
    private UnitOfMeasurement unit;
    private Order order;
    private List<Order> orders;

    @BeforeEach
    public void setup() {
        Address address = Address.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        customer = Customer.builder()
                .id(1L)
                .dni("123456789")
                .fullName("Jane Smith")
                .phone("555-1234-1")
                .address(address)
                .build();

        supplier = Supplier.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        unit = UnitOfMeasurement.builder()
                .id(1L)
                .name("Piece")
                .build();

        List<Product> products = List.of(
                Product.builder()
                        .id(11L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Coffee Maker")
                        .salePrice(BigDecimal.valueOf(50))
                        .build(),
                Product.builder()
                        .id(12L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Desk Chair")
                        .salePrice(BigDecimal.valueOf(100))
                        .build()
        );

        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(11L, 10L);
            put(12L, 10L);
        }};

        order = buildOrder(1L, products, productsWithQuantities);
        orders = buildOrders();
    }

    private Order buildOrder(Long id, List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(id);
        order.setOrderedAt(LocalDateTime.now());
        order.deliver();
        return order;
    }

    private List<Order> buildOrders() {

        var productList1 = List.of(
                Product.builder()
                        .id(11L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Coffee Maker")
                        .salePrice(BigDecimal.valueOf(50))
                        .build(),
                Product.builder()
                        .id(12L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Desk Chair")
                        .salePrice(BigDecimal.valueOf(100))
                        .build()
        );

        HashMap<Long, Long> productsWithQuantities1 = new HashMap<>() {{
            put(11L, 10L);
            put(12L, 10L);
        }};


        var productList2 = List.of(
                Product.builder()
                        .id(13L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Washing Machine")
                        .salePrice(BigDecimal.valueOf(400))
                        .build(),
                Product.builder()
                        .id(14L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Office Desk")
                        .salePrice(BigDecimal.valueOf(120))
                        .build()
        );

        HashMap<Long, Long> productsWithQuantities2 = new HashMap<>() {{
            put(13L, 10L);
            put(14L, 10L);
        }};

        return List.of(
                buildOrder(6L, productList1, productsWithQuantities1),
                buildOrder(7L, productList2, productsWithQuantities2)
        );
    }


    @Test
    public void entityToDto() {
        var dto = mapper.entityToDto(order);

        assertNotNull(dto);
        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getCustomer().getFullName(), dto.getCustomer());
        assertEquals(order.getTotal(), dto.getTotal());
        assertEquals(order.getOrderedAt(), dto.getOrderedAt());
        assertEquals(order.isDelivered(), dto.isDelivered());
        assertEquals(order.getDeliveredAt(), dto.getDeliveredAt());
    }

    @Test
    public void entityToDetailsDto() {
        var dto = mapper.entityToDetailsDto(order);

        assertNotNull(dto);
        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getTotal().doubleValue(), dto.getTotal());

        assertNotNull(dto.getCustomer());
        assertEqualsCustomer(order.getCustomer(), dto.getCustomer());

        assertNotNull(dto.getItems());
        assertEqualsItems(order.getItems(), dto.getItems());
        assertEquals(order.getOrderedAt(), dto.getOrderedAt());

        assertEquals(order.isDelivered(), dto.isDelivered());
        assertEquals(order.getDeliveredAt(), dto.getDeliveredAt());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var ordersDto = orders.stream().map(mapper::entityToDto).toList();

        Page<Order> page = new PageImpl<>(orders, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(ordersDto);
    }


    private void assertEqualsCustomer(Customer expected, CustomerDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDni(), actual.getDni());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getPhone(), actual.getPhone());
    }

    private void assertEqualsItem(OrderItem expected, OrderItemDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getProductUnit(), actual.getProductUnit());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getPrice().doubleValue(), actual.getPrice());
        assertEquals(expected.getTotal().doubleValue(), actual.getTotal());
    }

    private void assertEqualsItems(List<OrderItem> expected, List<OrderItemDto> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            var e = expected.get(i);
            var a = actual.get(i);
            assertEqualsItem(e, a);
        }
    }
}
