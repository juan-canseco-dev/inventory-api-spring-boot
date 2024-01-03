package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.CategoryController;
import com.jcanseco.inventoryapi.controllers.CustomerController;
import com.jcanseco.inventoryapi.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerUnitTests {

    @MockBean
    private CustomerService customerService;



    @Test
    public void demo() {

    }

}
