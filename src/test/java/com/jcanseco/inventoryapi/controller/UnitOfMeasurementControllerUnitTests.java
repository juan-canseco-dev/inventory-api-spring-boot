package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.UnitOfMeasurementController;
import com.jcanseco.inventoryapi.services.UnitService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = UnitOfMeasurementController.class)
public class UnitOfMeasurementControllerUnitTests {

    @MockBean
    private UnitService unitService;

    @Test
    public void test() {

    }



}
