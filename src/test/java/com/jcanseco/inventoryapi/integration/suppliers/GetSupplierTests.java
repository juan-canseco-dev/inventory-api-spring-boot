package com.jcanseco.inventoryapi.integration.suppliers;

import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.entities.SupplierAddress;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSupplierTests {
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
    public void getSupplierWhenSupplierExistsStatusShouldBeOk() throws Exception{

        var request = MockMvcRequestBuilders
                .get("/api/suppliers/" + savedSupplier.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(savedSupplier.getId()))
                .andExpect(jsonPath("$.companyName").value(savedSupplier.getCompanyName()))
                .andExpect(jsonPath("$.contactName").value(savedSupplier.getContactName()))
                .andExpect(jsonPath("$.contactPhone").value(savedSupplier.getContactPhone()))
                .andExpect(jsonPath("$.address", Matchers.notNullValue()))
                .andExpect(jsonPath("$.address.country").value(savedSupplier.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.state").value(savedSupplier.getAddress().getState()))
                .andExpect(jsonPath("$.address.city").value(savedSupplier.getAddress().getCity()))
                .andExpect(jsonPath("$.address.zipCode").value(savedSupplier.getAddress().getZipCode()))
                .andExpect(jsonPath("$.address.street").value(savedSupplier.getAddress().getStreet()));
    }

    @Test
    public void getSupplierWhenSupplierDoesNotExistsStatusShouldBeNotFound() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/api/suppliers/10000")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

}