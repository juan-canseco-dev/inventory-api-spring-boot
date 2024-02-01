package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.ResourceController;
import com.jcanseco.inventoryapi.security.resources.Resource;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
import com.jcanseco.inventoryapi.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
        @MockBean(CustomerService.class),
        @MockBean(CategoryService.class),
        @MockBean(ProductService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(RoleService.class)
})
@WebMvcTest(
        controllers = ResourceController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class ResourceControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService service;

    @Test
    public void getResourcesStatusShouldBeOk() throws Exception {
        List<Resource> resources = new ArrayList<>();
        when(service.getAllResources()).thenReturn(resources);
        mockMvc.perform(
                        get("/api/resources")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
