package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.services.ProductService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
public class ProductServiceUnitTests {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UnitOfMeasurementRepository unitRepository;
    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @InjectMocks
    private ProductService productService;
    private final Long supplierId  = 1L;
    private final Long categoryId = 1L;
    private final Long unitId = 1L;
    private Supplier supplier;
    private Category category;
    private UnitOfMeasurement unit;
    private List<Product> products;

    @BeforeEach
    public void setup() {

        supplier = Supplier.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(Address.builder()
                        .country("United States")
                        .state("California")
                        .city("San Francisco")
                        .zipCode("94105")
                        .street("123 Main St")
                        .build()
                )
                .build();

        category = Category.builder()
                .id(categoryId)
                .name("Electronics")
                .build();

        unit = UnitOfMeasurement.builder()
                .id(unitId)
                .name("Piece")
                .build();

        products = List.of(
                Product.builder()
                        .id(1L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Halo 2")
                        .quantity(50L)
                        .purchasePrice(BigDecimal.valueOf(19.99))
                        .salePrice(BigDecimal.valueOf(29.99))
                        .build(),
                Product.builder()
                        .id(1L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Halo 3")
                        .quantity(50L)
                        .purchasePrice(BigDecimal.valueOf(29.99))
                        .salePrice(BigDecimal.valueOf(39.99))
                        .build()
        );
    }

    @Test
    public void createProductCreateShouldReturnProductId() {

        var createdProductId = 10L;

        var createdProduct = Product.builder()
                .id(createdProductId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(29.99))
                .build();

        var dto = CreateProductDto.builder()
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("New Product")
                .purchasePrice(19.99)
                .salePrice(29.99)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(unitRepository.findById(unitId)).thenReturn(Optional.of(unit));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(createdProduct);

        var resultProductId = productService.createProduct(dto);
        assertEquals(createdProductId, resultProductId);
    }

    @Test
    public void createProductWhenSupplierNotExistsShouldThrowException() {
        var dto = CreateProductDto.builder()
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("New Product")
                .purchasePrice(19.99)
                .salePrice(29.99)
                .build();
        when(supplierRepository.findById(supplierId)).thenThrow(new DomainException("Supplier Not Found"));
        assertThrows(DomainException.class, () -> productService.createProduct(dto));
    }

    @Test
    public void createProductWhenCategoryNotExistsShouldThrowException() {
        var dto = CreateProductDto.builder()
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("New Product")
                .purchasePrice(19.99)
                .salePrice(29.99)
                .build();
        when(categoryRepository.findById(categoryId)).thenThrow(new DomainException("Category Not Found"));
        assertThrows(DomainException.class, () -> productService.createProduct(dto));
    }

    @Test
    public void createProductWhenUnitNotExistsShouldThrowException() {
        var dto = CreateProductDto.builder()
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("New Product")
                .purchasePrice(19.99)
                .salePrice(29.99)
                .build();
        when(unitRepository.findById(unitId)).thenThrow(new DomainException("Unit Of Measurement Not Found"));
        assertThrows(DomainException.class, () -> productService.createProduct(dto));
    }

    @Test
    public void createProductWhenPurchasePriceGreaterThanSalePriceShouldThrowException() {

        var dto = CreateProductDto.builder()
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("New Product")
                .purchasePrice(39.99)
                .salePrice(29.99)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(unitRepository.findById(unitId)).thenReturn(Optional.of(unit));

        assertThrows(DomainException.class, () -> productService.createProduct(dto));
    }

    @Test
    public void updateProductWhenProductExistsShouldUpdateSuccessful() {

    }

    @Test
    public void updateProductWhenProductDoNotExistsShouldThrowException() {

    }

    @Test
    public void updateProductWhenSupplierDoNotExistsShouldThrowException() {

    }

    @Test
    public void updateProductWhenCategoryDoNotExistsShouldThrowException() {

    }

    @Test
    public void updateProductWhenUnitDoNotExistsShouldThrowException() {

    }

    @Test
    public void updateProductWhenPurchasePriceIsGraterThanSalePriceShouldThrowException() {

    }

    @Test
    public void deleteProductWhenProductExistsShouldDeleteSuccessfully() {

    }

    @Test
    public void deleteProductWhenProductNotExistsShouldThrowException() {

    }

    @Test
    public void getProductByIdWhenProductExistsShouldReturnProduct() {

    }

    @Test
    public void getProductByIdWhenProductNotExistsShouldThrowException() {

    }

    @Test
    public void getProductsShouldReturnList() {

    }

    @Test
    public void getProductsPageShouldReturnList() {

    }
}
