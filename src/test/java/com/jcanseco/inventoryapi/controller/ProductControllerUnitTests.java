package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.ProductController;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDto;
import com.jcanseco.inventoryapi.dtos.products.ProductDetailsDto;
import com.jcanseco.inventoryapi.dtos.products.CreateProductDto;
import com.jcanseco.inventoryapi.dtos.products.GetProductsRequest;
import com.jcanseco.inventoryapi.dtos.products.UpdateProductDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
@MockBeans({
        @MockBean(CustomerService.class),
        @MockBean(CategoryService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class)
})
@WebMvcTest(
        controllers = ProductController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class ProductControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductService productService;

    private SupplierDto defaultSupplier() {
        return SupplierDto.builder()
                .id(1L)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-3")
                .build();
    }

    private CategoryDto defaultCategory() {
        return CategoryDto.builder()
                .id(1L)
                .name("Electronics")
                .build();
    }


    private UnitOfMeasurementDto defaultUnit() {
        return UnitOfMeasurementDto.builder()
                .id(1L)
                .name("Piece")
                .build();
    }

    @Test
    public void createProductWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("New Product")
                .purchasePrice(59.99)
                .salePrice(69.99)
                .build();

        when(productService.createProduct(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    public void createProductWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var dto = CreateProductDto.builder()
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("New Product")
                .purchasePrice(59.99)
                .salePrice(-1.0)
                .build();

        when(productService.createProduct(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProductWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var productId = 1L;


        var updateProductDto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Update Product")
                .purchasePrice(59.99)
                .salePrice(69.99)
                .build();

        doNothing().when(productService).updateProduct(updateProductDto);

        mockMvc.perform(
                        put("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateProductDto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateProductWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var productId = 1L;

        var updateProductDto = UpdateProductDto.builder()
                .productId(productId)
                .categoryId(1L)
                .unitId(1L)
                .name("Update Product")
                .purchasePrice(59.99)
                .salePrice(69.99)
                .build();

        doNothing().when(productService).updateProduct(updateProductDto);

        mockMvc.perform(
                        put("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateProductDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProductWhenModelAndPathIdAreDifferentStatusShouldBeBadRequest() throws Exception {
        var productId = 1L;

        var updateProductDto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Update Product")
                .purchasePrice(59.99)
                .salePrice(69.99)
                .build();

        doNothing().when(productService).updateProduct(updateProductDto);

        mockMvc.perform(
                        put("/api/products/5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateProductDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProductWhenProductDoNotExistsStatusShouldBeNotFound() throws Exception {

        var productId = 1L;

        var updateProductDto = UpdateProductDto.builder()
                .productId(productId)
                .supplierId(1L)
                .categoryId(1L)
                .unitId(1L)
                .name("Update Product")
                .purchasePrice(59.99)
                .salePrice(69.99)
                .build();

        doThrow(new NotFoundException(String.format("Product with the Id {%d} was not found.", productId)))
                .when(productService)
                .updateProduct(updateProductDto);

        mockMvc.perform(
                        put("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateProductDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProductWhenProductExistsStatusShouldBeNoContent() throws Exception {
        var productId = 1L;
        doNothing().when(productService).deleteProduct(productId);
        mockMvc.perform(
                        delete("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProductWhenProductDoNotExistsStatusShouldBeNotFound() throws Exception {
        var productId = 1L;
        doThrow(new NotFoundException(String.format("Product with the Id {%d} was not found.", productId)))
                .when(productService)
                .deleteProduct(productId);
        mockMvc.perform(
                        delete("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProductByIdWhenProductExistsStatusShouldBeOk() throws Exception {
        var productId = 1L;
        var productDto = ProductDetailsDto.builder()
                        .id(1L)
                        .supplier(defaultSupplier())
                        .category(defaultCategory())
                        .unit(defaultUnit())
                        .name("Product")
                        .purchasePrice(BigDecimal.valueOf(49.99))
                        .salePrice(BigDecimal.valueOf(59.99))
                        .stock(10L)
                        .build();
        when(productService.getProductById(productId)).thenReturn(productDto);
        mockMvc.perform(
                        get("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getProductByIdWhenProductDoNotExistsStatusShouldBeNotFound() throws Exception {
        var productId = 1L;
        doThrow(new NotFoundException(String.format("Product with the Id {%d} was not found.", productId)))
                .when(productService).getProductById(productId);
        mockMvc.perform(
                        get("/api/products/" + productId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProductsWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getProductsWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getProductsPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getProductsWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getProductsRequest = GetProductsRequest.builder()
                .build();

        var products = List.of(
                ProductDto.builder()
                        .id(1L)
                        .supplier("Xbox")
                        .category("Video Games")
                        .unit("Piece")
                        .name("Halo Infinite")
                        .purchasePrice(BigDecimal.valueOf(49.99))
                        .salePrice(BigDecimal.valueOf(59.99))
                        .stock(10L)
                        .build(),
                ProductDto.builder()
                        .id(1L)
                        .supplier("Xbox")
                        .category("Video Games")
                        .unit("Piece")
                        .name("Halo 3")
                        .purchasePrice(BigDecimal.valueOf(9.99))
                        .salePrice(BigDecimal.valueOf(19.99))
                        .stock(10L)
                        .build()
                );

        when(productService.getProducts(getProductsRequest)).thenReturn(products);

        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getProductsPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getProductsPageRequest = GetProductsRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var products = List.of(
                ProductDto.builder()
                        .id(1L)
                        .supplier("Xbox")
                        .category("Video Games")
                        .unit("Piece")
                        .name("Halo Infinite")
                        .purchasePrice(BigDecimal.valueOf(49.99))
                        .salePrice(BigDecimal.valueOf(59.99))
                        .stock(10L)
                        .build()
        );

        var pagedList = new PagedList<>(products, 1, 1, 2, 2);

        when(productService.getProductsPaged(getProductsPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getProductsPageRequest.getPageNumber().toString())
                                .param("pageSize", getProductsPageRequest.getPageSize().toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.items", notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
