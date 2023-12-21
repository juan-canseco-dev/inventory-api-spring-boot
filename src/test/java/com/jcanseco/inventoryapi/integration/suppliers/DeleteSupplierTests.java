package com.jcanseco.inventoryapi.integration.suppliers;

import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteSupplierTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepository repository;
    private Supplier savedSupplier;

    @BeforeEach
    public void setup() {

        var address = SupplierAddress
                .builder()
                .country("MX")
                .state("SON")
                .city("HMO")
                .zipCode("83")
                .street("CENT")
                .build();

        var newSupplier = Supplier.builder()
                .companyName("ABC")
                .contactName("John")
                .contactPhone("1234")
                .address(address)
                .build();

        savedSupplier = repository.saveAndFlush(newSupplier);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void deleteSupplierWhenSupplierExistsStatusShouldBeOk() throws Exception {

        var supplierId = savedSupplier.getId();

        var request = MockMvcRequestBuilders
                .delete("/api/suppliers/" + supplierId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var supplierOpt = repository.findById(supplierId);
        assertTrue(supplierOpt.isEmpty());
    }

    @Test
    public void deleteSupplierWhenSupplierDoestNotExistsStatusShouldBeNotFound() throws Exception {

        var supplierId = 100000L;

        var request = MockMvcRequestBuilders
                .delete("/api/suppliers/" + supplierId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
