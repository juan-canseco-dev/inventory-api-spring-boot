package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.orders.CreateOrderDto;
import com.jcanseco.inventoryapi.dtos.orders.GetOrdersRequest;
import com.jcanseco.inventoryapi.dtos.orders.UpdateOrderDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.OrderMapper;
import com.jcanseco.inventoryapi.repositories.*;
import com.jcanseco.inventoryapi.services.OrderService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTests {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private OrderRepository orderRepository;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @Spy
    private IndexUtility indexUtility;
    @InjectMocks
    private OrderService service;
    @Captor
    private ArgumentCaptor<Order> orderArgCaptor;
    @Captor
    private ArgumentCaptor<List<Stock>> stocksArgCaptor;
    private final Long orderId = 1L;
    private final Long supplierId = 1L;
    private final Long customerId = 1L;

    private final HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
        put(9L, 10L);
        put(10L, 10L);
    }};

    private final HashMap<Long, Long> productsWithQuantitiesForUpdate = new HashMap<>() {{
        put(9L, 5L);
        put(10L, 5L);
    }};

    private Supplier supplier;
    private Customer customer;
    private List<Product> products;
    private List<Stock> stocks;
    private List<Stock> expectedStocks;

    @BeforeEach
    public void setup() {

        var unit = UnitOfMeasurement.builder()
                .id(1L)
                .name("Piece")
                .build();

        var category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        var address = Address.builder()
                .country("United States")
                .state("California")
                .city("San Francisco")
                .zipCode("94105")
                .street("123 Main St")
                .build();

        supplier = Supplier.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        customer = Customer.builder()
                .id(customerId)
                .dni("12345678")
                .phone("555-1234-1")
                .fullName("Jane Smith")
                .address(address)
                .build();

        products = List.of(
                Product.builder()
                        .id(9L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Vacuum Cleaner")
                        .purchasePrice(new BigDecimal("90.00"))
                        .salePrice(new BigDecimal("150.00"))
                        .build(),
                Product.builder()
                        .id(10L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Toaster")
                        .purchasePrice(new BigDecimal("25.00"))
                        .salePrice(new BigDecimal("40.00"))
                        .build()
        );

        stocks = List.of(
                Stock.builder()
                        .id(1L)
                        .product(products.get(0))
                        .productId(products.get(0).getId())
                        .quantity(30L)
                        .build(),
                Stock.builder()
                        .id(2L)
                        .product(products.get(1))
                        .productId(products.get(1).getId())
                        .quantity(30L)
                        .build()
        );

        expectedStocks = List.of(
                Stock.builder()
                        .id(1L)
                        .product(products.get(0))
                        .productId(products.get(0).getId())
                        .quantity(20L)
                        .build(),
                Stock.builder()
                        .id(2L)
                        .product(products.get(1))
                        .productId(products.get(1).getId())
                        .quantity(20L)
                        .build()
        );

    }

    @Test
    public void createOrderShouldReturnOrderId() {

        var savedOrder = Order.createNew(customer, products, productsWithQuantities);
        savedOrder.setId(orderId);

        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        when(customerRepository.findById(supplierId)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(productsWithQuantities.keySet())).thenReturn(products);
        when(orderRepository.saveAndFlush(any())).thenReturn(savedOrder);

        var resultId = service.createOrder(dto);
        assertEquals(orderId, resultId);

        verify(orderRepository, times(1)).saveAndFlush(any());

        verify(orderRepository).saveAndFlush(orderArgCaptor.capture());

        var newOrder = orderArgCaptor.getValue();
        assertNotNull(newOrder);

        assertEquals(savedOrder.getCustomer(), newOrder.getCustomer());
        assertEquals(savedOrder.getItems(), newOrder.getItems());
        assertEquals(savedOrder.getTotal(), newOrder.getTotal());
    }

    @Test
    public void createOrderWhenCustomerDoNotExistsShouldThrowException() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        var dto = CreateOrderDto.builder()
                .customerId(customerId)
                .productsWithQuantities(productsWithQuantities)
                .build();
        assertThrows(DomainException.class, () -> service.createOrder(dto));
    }

    @Test
    public void updateOrderWhenOrderExistsAndIsNotDeliveredShouldUpdate() {

        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(productsWithQuantitiesForUpdate)
                .build();

        var foundOrder = Order.createNew(customer, products, productsWithQuantities);
        foundOrder.setId(orderId);

        var expectedOrder = Order.createNew(customer, products, productsWithQuantities);
        expectedOrder.setId(orderId);
        expectedOrder.update(products, productsWithQuantitiesForUpdate);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(foundOrder));
        when(productRepository.findAllById(productsWithQuantitiesForUpdate.keySet())).thenReturn(products);
        when(orderRepository.saveAndFlush(any())).thenReturn(any());

        service.updateOrder(dto);

        verify(orderRepository, times(1)).saveAndFlush(any());

        verify(orderRepository).saveAndFlush(orderArgCaptor.capture());

        var updatedOrder = orderArgCaptor.getValue();
        assertNotNull(updatedOrder);
        assertEquals(expectedOrder, updatedOrder);
    }

    @Test
    public void updateOrderWhenOrderIsDeliveredShouldThrowException() {

        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(productsWithQuantitiesForUpdate)
                .build();

        var foundOrder = Order.createNew(customer, products, productsWithQuantities);
        foundOrder.setId(orderId);
        foundOrder.deliver();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(foundOrder));
        when(productRepository.findAllById(productsWithQuantitiesForUpdate.keySet())).thenReturn(products);

        assertThrows(DomainException.class, () -> service.updateOrder(dto));
    }

    @Test
    public void updateOrderWhenOrderNotExistsShouldThrowException() {
        var dto = UpdateOrderDto.builder()
                .orderId(orderId)
                .productsWithQuantities(productsWithQuantitiesForUpdate)
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateOrder(dto));
    }

    @Test
    public void deliverOrderWhenOrderIsNotDeliveredShouldDeliver() {

        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(stockRepository.findAll(any(Specification.class))).thenReturn(stocks);

        service.deliverOrder(orderId);

        verify(orderRepository, times(1)).saveAndFlush(any());
        verify(orderRepository).saveAndFlush(orderArgCaptor.capture());
        var updatedOrder = orderArgCaptor.getValue();
        assertTrue(updatedOrder.isDelivered());
        assertNotNull(updatedOrder.getDeliveredAt());

        verify(stockRepository, times(1)).saveAllAndFlush(any());
        verify(stockRepository).saveAllAndFlush(stocksArgCaptor.capture());
        var updatedStocks = stocksArgCaptor.getValue();

        assertThat(updatedStocks).hasSameElementsAs(expectedStocks);
    }

    @Test
    public void deliverOrderWhenOrderIsDeliveredShouldThrowException() {

        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(orderId);
        order.deliver();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(DomainException.class, () -> service.deliverOrder(orderId));
    }

    @Test
    public void deliverOrderWhenOrderNotExistsShouldThrowException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deliverOrder(orderId));
    }

    @Test
    public void deleteOrderWhenOrderExistsShouldDelete() {
        Order order = mock();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).delete(order);
        service.deleteOrder(orderId);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    public void deleteOrderWhenOrdersIsDeliveredShouldThrowException() {
        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(orderId);
        order.deliver();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(DomainException.class, () -> service.deleteOrder(orderId));
    }

    @Test
    public void deleteOrderWhenOrderNotExistsShouldThrowException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteOrder(orderId));
    }

    @Test
    public void getOrderWhenOrdersExistsShouldGet() {

        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(orderId);

        var expectedOrder = orderMapper.entityToDetailsDto(order);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        var result = service.getOrderById(orderId);
        assertEquals(expectedOrder, result);
    }

    @Test
    public void getOrderWhenOrderDoNotExistsShouldThrowException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getOrderById(orderId));
    }

    @Test
    public void getOrdersShouldReturnList() {

        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(orderId);

        var orders = List.of(
                order
        );

        var expectedResult = orders.stream().map(orderMapper::entityToDto).toList();
        var request = GetOrdersRequest.builder().build();
        Specification<Order> spec = any(Specification.class);

        when(orderRepository.findAll(spec)).thenReturn(orders);

        var result = service.getOrders(request);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getOrdersPageShouldReturnPagedList() {

        var totalOrdersInDb = 2;

        var totalPages = 2;

        var order = Order.createNew(customer, products, productsWithQuantities);
        order.setId(orderId);

        var orders = List.of(
                order
        );
        var expectedItems =  orders.stream().map(orderMapper::entityToDto).toList();


        var request = GetOrdersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        Specification<Order> mockSpec = any(Specification.class);
        PageRequest mockPageRequest = any();
        Page<Order> mockPage = new PageImpl<>(
                orders,
                Pageable.ofSize(1),
                totalOrdersInDb
        );

        when(orderRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = service.getOrdersPage(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalOrdersInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
