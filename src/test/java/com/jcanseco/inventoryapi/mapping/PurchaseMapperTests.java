package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.purchases.PurchaseItemDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PurchaseMapperTests {
    private final PurchaseMapper mapper = Mappers.getMapper(PurchaseMapper.class);

    private Purchase buildPurchase() {

        var address = SupplierAddress.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var supplier = Supplier.builder()
                .id(3L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var items = List.of(
                PurchaseItem.builder()
                        .id(1L)
                        .productName("Laptop")
                        .productUnit("Box")
                        .quantity(10L)
                        .price(BigDecimal.valueOf(10))
                        .total(BigDecimal.valueOf(100))
                        .build()

        );


        var total = items.stream()
                .map(PurchaseItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Purchase.builder()
                .id(5L)
                .supplier(supplier)
                .items(items)
                .total(total)
                .build();
    }

    @Test
    public void entityToDto() {

        var purchase = buildPurchase();
        var dto = mapper.entityToDto(purchase);

        assertNotNull(dto);
        assertEquals(purchase.getId(), dto.getId());
        assertEquals(purchase.getSupplier().getCompanyName(), dto.getSupplier());
        assertEquals(purchase.getTotal(), dto.getTotal());
    }

    @Test
    public void entityToDetailsDto() {

        var purchase = buildPurchase();
        var dto = mapper.entityToDetailsDto(purchase);

        assertNotNull(dto);
        assertEquals(purchase.getId(), dto.getId());
        assertEquals(purchase.getTotal(), dto.getTotal());

        assertNotNull(dto.getSupplier());
        assertEqualsSupplier(purchase.getSupplier(), dto.getSupplier());

        assertNotNull(dto.getItems());
        assertEqualsItems(purchase.getItems(), dto.getItems());
    }

    private void assertEqualsSupplier(Supplier expected, SupplierDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCompanyName(), actual.getCompanyName());
        assertEquals(expected.getContactName(), actual.getContactName());
        assertEquals(expected.getContactPhone(), actual.getContactPhone());
    }

    private void assertEqualsItem(PurchaseItem expected, PurchaseItemDto actual) {
        assertEquals(expected.getId(), actual.getId());
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
