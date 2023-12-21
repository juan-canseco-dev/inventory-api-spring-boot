package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTests {
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    private Product buildProduct() {
        var category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        var unit = UnitOfMeasurement.builder()
                .id(2L)
                .name("Piece")
                .build();


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

        return Product.builder()
                .id(4L)
                .name("Laptop")
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .quantity(10L)
                .purchasePrice(BigDecimal.valueOf(20.99))
                .salePrice(BigDecimal.valueOf(30.99)).build();
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
