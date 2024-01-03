package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.ProductController;
import com.jcanseco.inventoryapi.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = ProductController.class)
public class ProductControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void demo() {

    }

}
