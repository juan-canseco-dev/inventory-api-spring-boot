package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.purchases.CreatePurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.GetPurchasesRequest;
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
    private PurchaseMapper purchaseMapper = Mappers.getMapper(PurchaseMapper.class);
    @Spy
    private IndexUtility indexUtility;
    @InjectMocks
    private PurchaseService service;
    @Captor
    private ArgumentCaptor<Purchase> purchaseArgCaptor;
    @Captor
    private ArgumentCaptor<List<Stock>> stocksArgCaptor;
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
                        .quantity(10L)
                        .build(),
                Stock.builder()
                        .id(2L)
                        .product(products.get(1))
                        .productId(products.get(1).getId())
                        .quantity(10L)
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

        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(purchaseId);

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(stockRepository.findAll(any(Specification.class))).thenReturn(stocks);

        service.receivePurchase(purchaseId);

        verify(purchaseRepository, times(1)).saveAndFlush(any());
        verify(purchaseRepository).saveAndFlush(purchaseArgCaptor.capture());
        var updatedPurchase = purchaseArgCaptor.getValue();
        assertTrue(updatedPurchase.isArrived());
        assertNotNull(updatedPurchase.getArrivedAt());

        verify(stockRepository, times(1)).saveAllAndFlush(any());
        verify(stockRepository).saveAllAndFlush(stocksArgCaptor.capture());
        var updatedStocks = stocksArgCaptor.getValue();

        assertThat(updatedStocks).hasSameElementsAs(expectedStocks);
    }
    @Test
    public void receivePurchaseWhenPurchaseIsArrivedShouldThrowException() {

        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(purchaseId);
        purchase.markAsArrived();

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        assertThrows(DomainException.class, () -> service.receivePurchase(purchaseId));
    }

    @Test
    public void receivePurchaseWhenPurchaseNotExistsShouldThrowException() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.receivePurchase(purchaseId));
    }

    @Test
    public void deletePurchaseWhenPurchaseExistsShouldDelete() {
        Purchase purchase = mock();
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        doNothing().when(purchaseRepository).delete(purchase);
        service.deletePurchase(purchaseId);
        verify(purchaseRepository, times(1)).delete(purchase);
    }

    @Test
    public void deleteWhenPurchaseIsArrivedShouldThrowException() {
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(purchaseId);
        purchase.markAsArrived();

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        assertThrows(DomainException.class, () -> service.deletePurchase(purchaseId));
    }

    @Test
    public void deletePurchaseWhenPurchaseNotExistsShouldThrowException() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deletePurchase(purchaseId));
    }

    @Test
    public void getPurchaseWhenPurchaseExistsShouldGet() {

        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(purchaseId);

        var expectedPurchase = purchaseMapper.entityToDetailsDto(purchase);
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        var result = service.getPurchaseById(purchaseId);
        assertEquals(expectedPurchase, result);
    }

    @Test
    public void getPurchaseWhenPurchaseDoNotExistsShouldThrowException() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getPurchaseById(purchaseId));
    }

    @Test
    public void getPurchasesShouldReturnList() {

        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(purchaseId);

        var purchases = List.of(
                purchase
        );

        var expectedResult = purchases.stream().map(purchaseMapper::entityToDto).toList();
        var request = GetPurchasesRequest.builder().build();
        Specification<Purchase> spec = any(Specification.class);

        when(purchaseRepository.findAll(spec)).thenReturn(purchases);

        var result = service.getPurchases(request);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getPurchasesPageShouldReturnPagedList() {

        var totalPurchasesInDb = 2;

        var totalPages = 2;

        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(purchaseId);

        var purchases = List.of(
                purchase
        );
        var expectedItems =  purchases.stream().map(purchaseMapper::entityToDto).toList();


        var request = GetPurchasesRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        Specification<Purchase> mockSpec = any(Specification.class);
        PageRequest mockPageRequest = any();
        Page<Purchase> mockPage = new PageImpl<>(
                purchases,
                Pageable.ofSize(1),
                totalPurchasesInDb
        );

        when(purchaseRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = service.getPurchasesPage(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalPurchasesInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
