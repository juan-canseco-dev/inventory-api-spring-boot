package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTests {
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    private Supplier supplier;
    private Category category;
    private UnitOfMeasurement unit;
    private Product product;

    @BeforeEach
    public void setup() {

        supplier = Supplier.builder()
                .id(3L)
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

        category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        unit = UnitOfMeasurement.builder()
                .id(2L)
                .name("Piece")
                .build();

        product = Product.builder()
                .id(4L)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .stock(Stock.builder().quantity(10L).build())
                .name("Laptop")
                .purchasePrice(BigDecimal.valueOf(20.99))
                .salePrice(BigDecimal.valueOf(30.99))
                .build();

    }

    @Test
    public void entityToDto() {

        var entity = product;
        var dto = mapper.entityToDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getStock().getQuantity(), dto.getStock());
        assertEquals(entity.getPurchasePrice().doubleValue(), dto.getPurchasePrice());
        assertEquals(entity.getSalePrice().doubleValue(), dto.getSalePrice());
        assertEquals(entity.getSupplier().getCompanyName(), dto.getSupplier());
        assertEquals(entity.getCategory().getName(), dto.getCategory());
        assertEquals(entity.getUnit().getName(), dto.getUnit());
    }

    @Test
    public void entityToDetailsDto() {

        var entity = product;
        var dto = mapper.entityToDetailsDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getStock().getQuantity(), dto.getStock());
        assertEquals(entity.getPurchasePrice().doubleValue(), dto.getPurchasePrice());
        assertEquals(entity.getSalePrice().doubleValue(), dto.getSalePrice());

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

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;


        var products = List.of(
                Product.builder()
                        .id(4L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Mouse")
                        .stock(Stock.builder().quantity(10L).build())
                        .salePrice(BigDecimal.valueOf(20.99))
                        .purchasePrice(BigDecimal.valueOf(30.99))
                        .build(),
                Product.builder()
                        .id(5L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Pc Gamer")
                        .stock(Stock.builder().quantity(50L).build())
                        .salePrice(BigDecimal.valueOf(599.99))
                        .purchasePrice(BigDecimal.valueOf(799.99))
                        .build()
        );

        var productsDto = products.stream().map(mapper::entityToDto).toList();

        Page<Product> page = new PageImpl<>(products, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(productsDto);
    }
}
