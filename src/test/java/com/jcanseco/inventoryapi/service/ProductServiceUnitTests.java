package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import com.jcanseco.inventoryapi.dtos.products.GetProductsRequest;
import com.jcanseco.inventoryapi.dtos.products.UpdateProductDto;
import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.services.ProductService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
                        .stock(Stock.builder().id(1L).quantity(50L).build())
                        .purchasePrice(BigDecimal.valueOf(19.99))
                        .salePrice(BigDecimal.valueOf(29.99))
                        .build(),
                Product.builder()
                        .id(1L)
                        .supplier(supplier)
                        .category(category)
                        .unit(unit)
                        .name("Halo 3")
                        .stock(Stock.builder().id(1L).quantity(50L).build())
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

        var productId = 10L;
        var updateSupplierId = 2L;
        var updateCategoryId = 2L;
        var updateUnitId = 2L;

        var updateSupplier = Supplier.builder()
                .id(updateSupplierId)
                .companyName("B Corp")
                .contactName("Jane Smith")
                .contactPhone("555-1234-9")
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

        var updateCategory = Category.builder()
                .id(updateCategoryId)
                .name("Computers")
                .build();

        var updateUnit = UnitOfMeasurement.builder()
                .id(updateUnitId)
                .name("Box")
                .build();

        var foundProduct = Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .stock(Stock.builder().id(1L).quantity(0L).build())
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(29.99))
                .build();


        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(updateSupplierId)
                .categoryId(updateCategoryId)
                .unitId(updateUnitId)
                .name("Update Product")
                .purchasePrice(29.99)
                .salePrice(39.99)
                .build();

        when(supplierRepository.findById(updateSupplierId)).thenReturn(Optional.of(updateSupplier));
        when(categoryRepository.findById(updateCategoryId)).thenReturn(Optional.of(updateCategory));
        when(unitRepository.findById(updateUnitId)).thenReturn(Optional.of(updateUnit));
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));

        productService.updateProduct(dto);
        verify(productRepository, times(1)).saveAndFlush(foundProduct);

        var productArgCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).saveAndFlush(productArgCaptor.capture());

        var updatedProduct = productArgCaptor.getValue();
        assertNotNull(updatedProduct);
        assertEquals(productId, updatedProduct.getId());
        assertEquals(dto.getName(), updatedProduct.getName());
        assertEquals(updateSupplier, updatedProduct.getSupplier());
        assertEquals(updateCategory, updatedProduct.getCategory());
        assertEquals(updateUnit, updatedProduct.getUnit());
        assertEquals(dto.getPurchasePrice(), updatedProduct.getPurchasePrice().doubleValue());
        assertEquals(dto.getSalePrice(), updatedProduct.getSalePrice().doubleValue());
    }

    @Test
    public void updateProductWhenProductDoNotExistsShouldThrowException() {
        var productId = 10000L;
        when(productRepository.findById(productId)).thenThrow(new NotFoundException("Product Not Found."));
        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    public void updateProductWhenSupplierDoNotExistsShouldThrowException() {
        var productId = 10L;
        var updateSupplierId = 2L;

        var foundProduct = Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .stock(Stock.builder().id(1L).quantity(0L).build())
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(29.99))
                .build();


        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(updateSupplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("Update Product")
                .purchasePrice(29.99)
                .salePrice(39.99)
                .build();

        when(supplierRepository.findById(updateSupplierId)).thenThrow(new DomainException("Supplier Not Found"));
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));

        assertThrows(DomainException.class, () -> productService.updateProduct(dto));
    }

    @Test
    public void updateProductWhenCategoryDoNotExistsShouldThrowException() {
        var productId = 10L;
        var updateCategoryId = 2L;

        var foundProduct = Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .stock(Stock.builder().id(1L).quantity(0L).build())
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(29.99))
                .build();


        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(supplierId)
                .categoryId(updateCategoryId)
                .unitId(unitId)
                .name("Update Product")
                .purchasePrice(29.99)
                .salePrice(39.99)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(updateCategoryId)).thenThrow(new DomainException("Category Not Found"));
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));

        assertThrows(DomainException.class, () -> productService.updateProduct(dto));
    }

    @Test
    public void updateProductWhenUnitDoNotExistsShouldThrowException() {
        var productId = 10L;
        var updateUnitId = 2L;

        var foundProduct = Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .stock(Stock.builder().id(1L).quantity(0L).build())
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(29.99))
                .build();


        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(updateUnitId)
                .name("Update Product")
                .purchasePrice(29.99)
                .salePrice(39.99)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(unitRepository.findById(unitId)).thenThrow(new DomainException("Unit Not Found."));
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));

        assertThrows(DomainException.class, () -> productService.updateProduct(dto));
    }

    @Test
    public void updateProductWhenPurchasePriceIsGraterThanSalePriceShouldThrowException() {

        var productId = 10L;

        var foundProduct = Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .stock(Stock.builder().id(1L).quantity(0L).build())
                .purchasePrice(BigDecimal.valueOf(19.99))
                .salePrice(BigDecimal.valueOf(29.99))
                .build();


        var dto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(supplierId)
                .categoryId(categoryId)
                .unitId(unitId)
                .name("Update Product")
                .purchasePrice(59.99)
                .salePrice(39.99)
                .build();

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(unitRepository.findById(unitId)).thenReturn(Optional.of(unit));
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));

        assertThrows(DomainException.class, () -> productService.updateProduct(dto));
    }

    @Test
    public void deleteProductWhenProductExistsShouldDeleteSuccessfully() {
        var productId = 1L;
        Product mockProduct = mock(Product.class);
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        doNothing().when(productRepository).delete(mockProduct);
        productService.deleteProduct(productId);
        verify(productRepository, times(1)).delete(mockProduct);
    }

    @Test
    public void deleteProductWhenProductNotExistsShouldThrowException() {
        var productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    public void getProductByIdWhenProductExistsShouldReturnProduct() {

        var productId = 1L;

        var foundProduct = Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name("New Product")
                .stock(Stock.builder().id(1L).quantity(0L).build())
                .purchasePrice(BigDecimal.valueOf(29.99))
                .salePrice(BigDecimal.valueOf(39.99))
                .build();

        var expected = productMapper.entityToDetailsDto(foundProduct);
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));
        var result = productService.getProductById(productId);

        assertEquals(expected, result);
    }

    @Test
    public void getProductByIdWhenProductNotExistsShouldThrowException() {
        var productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    public void getProductsShouldReturnList() {

        var expectedResult = products.stream()
                .map(productMapper::entityToDto)
                .toList();

        var request = GetProductsRequest.builder().build();
        Specification<Product> mockSpec = any();
        when(productRepository.findAll(mockSpec)).thenReturn(products);
        var result = productService.getProducts(request);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertThat(result).hasSameElementsAs(expectedResult);
    }

    @Test
    public void getProductsPageShouldReturnList() {
        var totalProductsInDb = 4;

        var totalPages = 2;

        var expectedItems = products.stream()
                .map(productMapper::entityToDto)
                .toList();


        var request = GetProductsRequest.builder()
                .pageNumber(1)
                .pageSize(2)
                .build();

        Specification<Product> mockSpec = any();
        PageRequest mockPageRequest = any();
        Page<Product> mockPage = new PageImpl<>(
                products,
                Pageable.ofSize(2),
                totalProductsInDb
        );

        when(productRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = productService.getProductsPaged(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalProductsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
