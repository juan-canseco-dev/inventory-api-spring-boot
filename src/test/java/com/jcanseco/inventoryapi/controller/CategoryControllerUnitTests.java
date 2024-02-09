package com.jcanseco.inventoryapi.controller;


import com.jcanseco.inventoryapi.controllers.CategoryController;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.services.JwtService;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
import com.jcanseco.inventoryapi.security.services.UserService;
import com.jcanseco.inventoryapi.services.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        @MockBean(ProductService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(ResourceService.class),
        @MockBean(RoleService.class),
        @MockBean(UserService.class),
        @MockBean(JwtService.class)
})
@WebMvcTest(
        controllers = CategoryController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class CategoryControllerUnitTests {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createCategoryWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateCategoryDto.builder()
                .name("New Category")
                .build();

        when(categoryService.createCategory(dto)).thenReturn(1L);

        mockMvc.perform(
                post("/api/categories")
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
    public void createCategoryWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = CreateCategoryDto.builder()
                .name("")
                .build();

        when(categoryService.createCategory(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategoryWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var categoryId = 1L;

        var updateCategoryDto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Electronics & Computers")
                .build();

        doNothing().when(categoryService).updateCategory(updateCategoryDto);

        mockMvc.perform(
                        put("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCategoryDto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateCategoryWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var categoryId = 1L;

        var updateCategoryDto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name(" ")
                .build();

        doNothing().when(categoryService).updateCategory(updateCategoryDto);

        mockMvc.perform(
                        put("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCategoryDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategoryWhenModelAndPathIdAreDifferentStatusShouldBeBadRequest() throws Exception {
        var categoryId = 1L;

        var updateCategoryDto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Electronics & Computers")
                .build();

        doNothing().when(categoryService).updateCategory(updateCategoryDto);

        mockMvc.perform(
                        put("/api/categories/5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCategoryDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategoryWhenCategoryDoNotExistsStatusShouldBeNotFound() throws Exception {

        var categoryId = 1L;

        var updateCategoryDto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Electronics & Computers")
                .build();

        doThrow(new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)))
                .when(categoryService)
                .updateCategory(updateCategoryDto);

        mockMvc.perform(
                        put("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateCategoryDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCategoryWhenCategoryExistsStatusShouldBeNoContent() throws Exception {
        var categoryId = 1L;
        doNothing().when(categoryService).deleteCategory(categoryId);
        mockMvc.perform(
                        delete("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCategoryWhenCategoryDoNotExistsStatusShouldBeNotFound() throws Exception {
        var categoryId = 1L;
        doThrow(new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)))
                .when(categoryService)
                .deleteCategory(categoryId);
        mockMvc.perform(
                        delete("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCategoryByIdWhenCategoryExistsStatusShouldBeOk() throws Exception {
        var categoryId = 1L;
        var categoryDto = CategoryDto.builder()
                .id(categoryId)
                .name("Electronics & VideoGames")
                .build();
        when(categoryService.getCategoryById(categoryId)).thenReturn(categoryDto);
        mockMvc.perform(
                        get("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoryByIdWhenCategoryDoNotExistsStatusShouldBeNotFound() throws Exception {
        var categoryId = 1L;
        doThrow(new NotFoundException(String.format("Category with the Id {%d} was not found.", categoryId)))
                .when(categoryService).getCategoryById(categoryId);
        mockMvc.perform(
                        get("/api/categories/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCategoriesWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoriesWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoriesPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoriesWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getCategoriesRequest = GetCategoriesRequest.builder()
                .build();

        var categories = List.of(
                CategoryDto.builder().id(1L).name("Electronics").build(),
                CategoryDto.builder().id(2L).name("Home & Garden").build()
        );

        when(categoryService.getCategories(getCategoriesRequest)).thenReturn(categories);

        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getCategoriesPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getCategoriesPageRequest = GetCategoriesRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var categories = List.of(
                CategoryDto.builder().id(1L).name("Electronics").build()
        );

        var pagedList = new PagedList<>(categories, 1, 1, 2, 2);

        when(categoryService.getCategoriesPaged(getCategoriesPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/categories")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getCategoriesPageRequest.getPageNumber().toString())
                                .param("pageSize", getCategoriesPageRequest.getPageSize().toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }
}
