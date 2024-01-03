package com.jcanseco.inventoryapi.controller;


import com.jcanseco.inventoryapi.controllers.CategoryController;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerUnitTests {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private CreateCategoryDto createCategoryDto;

    @BeforeEach
    public void setup() {
        createCategoryDto = CreateCategoryDto.builder()
                .name("New Category")
                .build();
    }

    @Test
    public void createCategoryWhenModelIsValidStatusShouldBeCreated() throws Exception {


    }

    @Test
    public void createCategoryWhenModelIsInvalidStatusShouldBeBadRequest() {

    }


}
