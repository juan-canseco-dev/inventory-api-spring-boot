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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PurchaseMapperTests {
    private final PurchaseMapper mapper = Mappers.getMapper(PurchaseMapper.class);

    private Purchase purchase;

    @BeforeEach
    public void setup() {
        Supplier supplier = Supplier.builder()
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

        var item1Price = new BigDecimal("9.99");
        var item1Quantity =  10L;
        var item1Total = item1Price.multiply(BigDecimal.valueOf(item1Quantity));

        var item1 = PurchaseItem.builder()
                .id(1L)
                .productName("Mouse")
                .productUnit("Piece")
                .price(item1Price)
                .quantity(item1Quantity)
                .total(item1Total)
                .build();


        var item2Price = new BigDecimal("19.99");
        var item2Quantity =  20L;
        var item2Total = item1Price.multiply(BigDecimal.valueOf(item2Quantity));

        var item2 = PurchaseItem.builder()
                .id(2L)
                .productName("Keyboard")
                .productUnit("Piece")
                .price(item2Price)
                .quantity(item2Quantity)
                .total(item2Total)
                .build();

        var items = List.of(
                item1,
                item2
        );
        var orderedAt = LocalDateTime.now();
        var total = items.stream()
                .map(PurchaseItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchase = Purchase.builder()
                .id(1L)
                .supplier(supplier)
                .items(items)
                .total(total)
                .orderedAt(orderedAt)
                .arrived(true)
                .arrivedAt(orderedAt.plusHours(5))
                .build();
    }
    @Test
    public void entityToDto() {

        var entity = purchase;
        var dto = mapper.entityToDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getSupplier().getCompanyName(), dto.getSupplier());
        assertEquals(entity.getTotal(), dto.getTotal());
        assertEquals(entity.getOrderedAt(), dto.getOrderedAt());
        assertEquals(entity.isArrived(), dto.isArrived());
        assertEquals(entity.getArrivedAt(), dto.getArrivedAt());
    }

    @Test
    public void entityToDetailsDto() {

        var entity = purchase;
        var dto = mapper.entityToDetailsDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTotal(), dto.getTotal());

        assertNotNull(dto.getSupplier());
        assertEqualsSupplier(entity.getSupplier(), dto.getSupplier());

        assertNotNull(dto.getItems());
        assertEqualsItems(entity.getItems(), dto.getItems());
        assertEquals(entity.getOrderedAt(), dto.getOrderedAt());

        assertEquals(entity.isArrived(), dto.isArrived());
        assertEquals(entity.getArrivedAt(), dto.getArrivedAt());
    }


    private List<Purchase> buildPurchases() {

        var items1 = List.of(
                PurchaseItem.builder()
                        .productId(11L)
                        .productName("Coffee Maker")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("50.00"))
                        .total(new BigDecimal("500.00"))
                        .build(),

                PurchaseItem.builder()
                        .productId(12L)
                        .productName("Desk Chair")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("100.00"))
                        .total(new BigDecimal("1000.00"))
                        .build()
                );

        var purchase1 = Purchase.builder()
                .id(6L)
                .supplier(Supplier.builder().build())
                .items(items1)
                .total(new BigDecimal("1500.00"))
                .orderedAt(LocalDateTime.now())
                .arrivedAt(LocalDateTime.now().plusHours(10))
                .arrived(true)
                .build();

        // items 2

        var items2 = List.of(
                PurchaseItem.builder()
                        .productId(13L)
                        .productName("Washing Machine")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("400.00"))
                        .total(new BigDecimal("4000.00"))
                        .build(),

                PurchaseItem.builder()
                        .productId(14L)
                        .productName("Office Desk")
                        .productUnit("Piece")
                        .quantity(10L)
                        .price(new BigDecimal("120.00"))
                        .total(new BigDecimal("1200.00"))
                        .build()
        );

        var purchase2 = Purchase.builder()
                .id(7L)
                .supplier(Supplier.builder().build())
                .items(items2)
                .total(new BigDecimal("5200.00"))
                .orderedAt(LocalDateTime.now())
                .arrivedAt(LocalDateTime.now().plusHours(10))
                .arrived(true)
                .build();


        return List.of(purchase1, purchase2);
    }


    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var purchases = buildPurchases();

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
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getTotal(), actual.getTotal());
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
