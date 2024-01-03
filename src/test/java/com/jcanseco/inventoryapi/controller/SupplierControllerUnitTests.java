package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.SupplierController;
import com.jcanseco.inventoryapi.services.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = SupplierController.class)
public class SupplierControllerUnitTests {

    @MockBean
    private SupplierService supplierService;

    @Test
    public void demo() {

    }


}
