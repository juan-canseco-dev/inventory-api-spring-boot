package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.dtos.purchases.PurchaseItemDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

public class PurchaseMapperTests {
    private final PurchaseMapper mapper = Mappers.getMapper(PurchaseMapper.class);

    private Purchase buildPurchase() {

        var supplier = newSupplier(
                1L,
                "ABC Corp",
                "John Doe",
                "555-1234-1",
                newAddress(
                 "Mexico",
                 "Sonora",
                 "Hermosillo",
                 "83200",
                 "Center"
                )
        );

        var items = List.of(
                newPurchaseItem(
                        1L,
                        70L,
                        "Laptop",
                        "Box",
                        10L,
                        9.99
                )
        );

        var createdAt = LocalDateTime.now();

        return newPurchase(5L,supplier, items, createdAt);
    }

    @Test
    public void entityToDto() {

        var purchase = buildPurchase();
        var dto = mapper.entityToDto(purchase);

        assertNotNull(dto);
        assertEquals(purchase.getId(), dto.getId());
        assertEquals(purchase.getSupplier().getCompanyName(), dto.getSupplier());
        assertEquals(purchase.getTotal(), dto.getTotal());
        assertEquals(purchase.getCreatedAt(), dto.getCreatedAt());
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
        assertEquals(purchase.getCreatedAt(), dto.getCreatedAt());
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
