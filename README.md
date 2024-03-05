# Inventory Management System
Inventory API with basic functionality, comprising units and integration tests.
## Features
This api has the following features:
- Sign In (JWT AUTH)
- Permission Based Authorization
- CRUD Operations for Users
- CRUD Operatios for Roles
- CRUD Operations for Suppliers
- CURD Operations for Categories 
- CRUD Operations for Products
- CRUD Purchases
- CRUD Orders

## Example of Service 
```java
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IndexUtility indexUtility;

    @Transactional
    public Long createOrder(CreateOrderDto dto) {
        var customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new DomainException(String.format("Customer with the Id : {%d} was not found.", dto.getCustomerId())));

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var savedOrder = orderRepository.saveAndFlush(
                Order.createNew(customer, products, productsWithQuantities)
        );

        return savedOrder.getId();
    }
}
```
## Service Tests 
```java
@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTests {
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
}
```
## To do
- Create a front-end
- Refactor test

## Libraries used
- JPA
- TestContainers
- MapStruct




