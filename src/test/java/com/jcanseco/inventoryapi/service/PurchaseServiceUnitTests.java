package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.PurchaseRepository;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.services.PurchaseService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class PurchaseServiceUnitTests {
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Spy
    private PurchaseMapper purchaseMapper;
    @Spy
    private IndexUtility indexUtility;
    @InjectMocks
    private PurchaseService service;
    private final Long purchaseId = 1L;
    private final Long supplier1Id = 1L;
    private final Long supplier2Id = 2L;
    private final HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
        put(9L, 10L);
        put(10L, 10L);
    }};

    private Supplier supplier1;
    private Supplier supplier2;
    private List<Product> products;

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

        supplier1 = Supplier.builder()
                .id(supplier1Id)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        supplier2 = Supplier.builder()
                .id(supplier2Id)
                .companyName("Tech Solutions Inc")
                .contactName("Alice Brown")
                .contactPhone("555-1234-4")
                .address(address)
                .build();

        products = List.of(
                Product.builder()
                        .id(9L)
                        .supplier(supplier1)
                        .category(category)
                        .unit(unit)
                        .name("Vacuum Cleaner")
                        .purchasePrice(new BigDecimal("90.00"))
                        .salePrice(new BigDecimal("150.00"))
                        .build(),
                Product.builder()
                        .id(10L)
                        .supplier(supplier1)
                        .category(category)
                        .unit(unit)
                        .name("Toaster")
                        .purchasePrice(new BigDecimal("25.00"))
                        .salePrice(new BigDecimal("40.00"))
                        .build()
                );
    }

    @Test
    public void createPurchaseShouldReturnProductId() {

    }

    @Test
    public void createPurchaseWhenSupplierDoNotExistsShouldThrowException() {

    }

    @Test
    public void updatePurchaseWhenPurchaseExistsAndIsNotArrivedShouldUpdate() {

    }

    @Test
    public void updatePurchaseWhenPurchaseIsArrivedShouldThrowException() {

    }

    @Test
    public void updatePurchaseWhenPurchaseNotExistsShouldThrowException() {

    }

    @Test
    public void receivePurchaseWhenPurchaseIsNotArrivedShouldReceive() {

    }

    @Test
    public void receivePurchaseWhenPurchaseIsArrivedShouldThrowException() {

    }

    @Test
    public void receivePurchaseWhenPurchaseNotExistsShouldThrowException() {

    }

    @Test
    public void deletePurchaseWhenPurchaseExistsShouldDelete() {

    }

    @Test
    public void deletePurchaseWhenPurchaseNotExistsShouldThrowException() {

    }

    @Test
    public void getPurchaseWhenPurchaseExistsShouldGet() {

    }

    @Test
    public void getPurchaseWhenPurchaseDoNotExistsShouldThrowException() {

    }

    @Test
    public void getPurchasesShouldReturnList() {

    }

    @Test
    public void getPurchasesPageShouldReturnPagedList() {

    }
}
