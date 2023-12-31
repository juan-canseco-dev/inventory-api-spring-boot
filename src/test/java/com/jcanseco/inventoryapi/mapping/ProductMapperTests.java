package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

public class ProductMapperTests {
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    private Product buildProduct() {

        var category = newCategory(1L, "Electronics");
        var unit = newUnit(2L, "Piece");

        var supplier = newSupplier(
                3L,
                "ABC Corp",
                "John Doe",
                "555-1234-1",
                newSupplierAddress(
                        "Mexico",
                        "Sonora",
                        "Hermosillo",
                        "83200",
                        "Center"
                )
        );

        return newProduct(
               4L,
                supplier,
                category,
                unit,
                "Laptop",
                10L,
                20.99,
                30.99
        );
    }

    @Test
    public void entityToDto() {

        var entity = buildProduct();
        var dto = mapper.entityToDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getQuantity(), dto.getQuantity());
        assertEquals(entity.getPurchasePrice(), dto.getPurchasePrice());
        assertEquals(entity.getSalePrice(), dto.getSalePrice());
        assertEquals(entity.getSupplier().getCompanyName(), dto.getSupplier());
        assertEquals(entity.getCategory().getName(), dto.getCategory());
        assertEquals(entity.getUnit().getName(), dto.getUnit());
    }

    @Test
    public void entityToDetailsDto() {

        var entity = buildProduct();
        var dto = mapper.entityToDetailsDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getQuantity(), dto.getQuantity());
        assertEquals(entity.getPurchasePrice(), dto.getPurchasePrice());
        assertEquals(entity.getSalePrice(), dto.getSalePrice());

        assertNotNull(dto.getCategory());
        assertEquals(entity.getCategory().getId(), dto.getCategory().getId());
        assertEquals(entity.getCategory().getName(), dto.getCategory().getName());

        assertNotNull(dto.getUnit());
        assertEquals(entity.getUnit().getId(), dto.getUnit().getId());
        assertEquals(entity.getUnit().getName(), dto.getUnit().getName());

        assertNotNull(dto.getSupplier());
        assertEquals(entity.getSupplier().getId(), dto.getSupplier().getId());
        assertEquals(entity.getSupplier().getCompanyName(), dto.getSupplier().getCompanyName());
        assertEquals(entity.getSupplier().getContactName(), dto.getSupplier().getContactName());
        assertEquals(entity.getSupplier().getContactPhone(), dto.getSupplier().getContactPhone());
    }
}
