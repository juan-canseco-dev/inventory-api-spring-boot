package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.purchases.PurchaseItemDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
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

public class PurchaseMapperTests {
    private final PurchaseMapper mapper = Mappers.getMapper(PurchaseMapper.class);
    private Supplier supplier;
    private UnitOfMeasurement unit;
    private Purchase purchase;

    private List<Purchase> purchases;

    @BeforeEach
    public void setup() {
        supplier = Supplier.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(
                        Address.builder()
                                .country("Mexico")
                                .state("Sonora")
                                .city("Hermosillo")
                                .zipCode("83200")
                                .street("Center")
                                .build()
                )
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
                        .purchasePrice(BigDecimal.valueOf(50))
                        .build(),
                Product.builder()
                        .id(12L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Desk Chair")
                        .purchasePrice(BigDecimal.valueOf(100))
                        .build()
        );

        HashMap<Long, Long> productsWithQuantities = new HashMap<>() {{
            put(11L, 10L);
            put(12L, 10L);
        }};

        purchase = buildPurchase(1L, products, productsWithQuantities);
        purchases = buildPurchases();
    }

    private Purchase buildPurchase(Long id, List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        var purchase = Purchase.createNew(supplier, products, productsWithQuantities);
        purchase.setId(id);
        purchase.setOrderedAt(LocalDateTime.now());
        purchase.markAsArrived();
        return purchase;
    }

    private List<Purchase> buildPurchases() {

        var productList1 = List.of(
                Product.builder()
                        .id(11L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Coffee Maker")
                        .purchasePrice(BigDecimal.valueOf(50))
                        .build(),
                Product.builder()
                        .id(12L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Desk Chair")
                        .purchasePrice(BigDecimal.valueOf(100))
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
                        .purchasePrice(BigDecimal.valueOf(400))
                        .build(),
                Product.builder()
                        .id(14L)
                        .supplier(supplier)
                        .unit(unit)
                        .name("Office Desk")
                        .purchasePrice(BigDecimal.valueOf(120))
                        .build()
        );

        HashMap<Long, Long> productsWithQuantities2 = new HashMap<>() {{
            put(13L, 10L);
            put(14L, 10L);
        }};

        return List.of(
                buildPurchase(6L, productList1, productsWithQuantities1),
                buildPurchase(7L, productList2, productsWithQuantities2)
        );
    }


    @Test
    public void entityToDto() {
        var dto = mapper.entityToDto(purchase);

        assertNotNull(dto);
        assertEquals(purchase.getId(), dto.getId());
        assertEquals(purchase.getSupplier().getCompanyName(), dto.getSupplier());
        assertEquals(purchase.getTotal().doubleValue(), dto.getTotal());
        assertEquals(purchase.getOrderedAt(), dto.getOrderedAt());
        assertEquals(purchase.isArrived(), dto.isArrived());
        assertEquals(purchase.getArrivedAt(), dto.getArrivedAt());
    }

    @Test
    public void entityToDetailsDto() {
        var dto = mapper.entityToDetailsDto(purchase);

        assertNotNull(dto);
        assertEquals(purchase.getId(), dto.getId());
        assertEquals(purchase.getTotal().doubleValue(), dto.getTotal());

        assertNotNull(dto.getSupplier());
        assertEqualsSupplier(purchase.getSupplier(), dto.getSupplier());

        assertNotNull(dto.getItems());
        assertEqualsItems(purchase.getItems(), dto.getItems());
        assertEquals(purchase.getOrderedAt(), dto.getOrderedAt());

        assertEquals(purchase.isArrived(), dto.isArrived());
        assertEquals(purchase.getArrivedAt(), dto.getArrivedAt());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var purchasesDto = purchases.stream().map(mapper::entityToDto).toList();

        Page<Purchase> page = new PageImpl<>(purchases, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(purchasesDto);
    }


    private void assertEqualsSupplier(Supplier expected, SupplierDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCompanyName(), actual.getCompanyName());
        assertEquals(expected.getContactName(), actual.getContactName());
        assertEquals(expected.getContactPhone(), actual.getContactPhone());
    }

    private void assertEqualsItem(PurchaseItem expected, PurchaseItemDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getProductUnit(), actual.getProductUnit());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getPrice().doubleValue(), actual.getPrice());
        assertEquals(expected.getTotal().doubleValue(), actual.getTotal());
    }

    private void assertEqualsItems(List<PurchaseItem> expected, List<PurchaseItemDto> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            var e = expected.get(i);
            var a = actual.get(i);
            assertEqualsItem(e, a);
        }
    }
}
