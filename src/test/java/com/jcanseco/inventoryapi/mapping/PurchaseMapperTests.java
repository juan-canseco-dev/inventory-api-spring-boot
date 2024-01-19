package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.purchases.PurchaseItemDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
