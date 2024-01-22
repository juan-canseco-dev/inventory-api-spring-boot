package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.purchases.CreatePurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.UpdatePurchaseDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.PurchaseRepository;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.services.PurchaseService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private final Long supplierId = 1L;

    private final HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
        put(9L, 10L);
        put(10L, 10L);
    }};

    private final HashMap<Long, Long> productsWithQuantitiesForUpdate = new HashMap<>() {{
        put(9L, 5L);
        put(10L, 5L);
    }};

    private Supplier supplier;
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

        supplier = Supplier.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
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
    }

    @Test
    public void createPurchaseShouldReturnProductId() {

        var savedPurchase = Purchase.createNew(supplier, products, productsWithQuantities);
        savedPurchase.setId(purchaseId);

        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
                .productsWithQuantities(productsWithQuantities)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(productRepository.findAllById(productsWithQuantities.keySet())).thenReturn(products);
        when(purchaseRepository.saveAndFlush(any())).thenReturn(savedPurchase);

        var resultId = service.createPurchase(dto);
        assertEquals(purchaseId, resultId);

        verify(purchaseRepository, times(1)).saveAndFlush(any());

        var purchaseArgCaptor = ArgumentCaptor.forClass(Purchase.class);
        verify(purchaseRepository).saveAndFlush(purchaseArgCaptor.capture());

        var newPurchase = purchaseArgCaptor.getValue();
        assertNotNull(newPurchase);

        assertEquals(savedPurchase.getSupplier(), newPurchase.getSupplier());
        assertEquals(savedPurchase.getItems(), newPurchase.getItems());
        assertEquals(savedPurchase.getTotal(), newPurchase.getTotal());
    }

    @Test
    public void createPurchaseWhenSupplierDoNotExistsShouldThrowException() {
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());
        var dto = CreatePurchaseDto.builder()
                .supplierId(supplierId)
                .productsWithQuantities(productsWithQuantities)
                .build();
        assertThrows(DomainException.class, () -> service.createPurchase(dto));
    }

    @Test
    public void updatePurchaseWhenPurchaseExistsAndIsNotArrivedShouldUpdate() {

        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(productsWithQuantitiesForUpdate)
                .build();

        var foundPurchase = Purchase.createNew(supplier, products, productsWithQuantities);
        foundPurchase.setId(purchaseId);

        var expectedPurchase = Purchase.createNew(supplier, products, productsWithQuantities);
        expectedPurchase.setId(purchaseId);
        expectedPurchase.update(products, productsWithQuantitiesForUpdate);

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(foundPurchase));
        when(productRepository.findAllById(productsWithQuantitiesForUpdate.keySet())).thenReturn(products);
        when(purchaseRepository.saveAndFlush(any())).thenReturn(any());

        service.updatePurchase(dto);

        verify(purchaseRepository, times(1)).saveAndFlush(any());

        var purchaseArgCaptor = ArgumentCaptor.forClass(Purchase.class);
        verify(purchaseRepository).saveAndFlush(purchaseArgCaptor.capture());

        var updatedPurchase = purchaseArgCaptor.getValue();
        assertNotNull(updatedPurchase);
        assertEquals(expectedPurchase, updatedPurchase);
    }

    @Test
    public void updatePurchaseWhenPurchaseIsArrivedShouldThrowException() {

        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(productsWithQuantitiesForUpdate)
                .build();

        var foundPurchase = Purchase.createNew(supplier, products, productsWithQuantities);
        foundPurchase.setId(purchaseId);
        foundPurchase.markAsArrived();

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(foundPurchase));
        when(productRepository.findAllById(productsWithQuantitiesForUpdate.keySet())).thenReturn(products);

        assertThrows(DomainException.class, () -> service.updatePurchase(dto));
    }

    @Test
    public void updatePurchaseWhenPurchaseNotExistsShouldThrowException() {
        var dto = UpdatePurchaseDto.builder()
                .purchaseId(purchaseId)
                .productsWithQuantities(productsWithQuantitiesForUpdate)
                .build();
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updatePurchase(dto));
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
