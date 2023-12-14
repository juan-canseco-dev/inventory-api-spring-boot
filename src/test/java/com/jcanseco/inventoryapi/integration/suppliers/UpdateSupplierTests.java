package com.jcanseco.inventoryapi.integration.suppliers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.hamcrest.Matchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateSupplierTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
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
    public void updateSupplierWhenSupplierExistsAndModelIsValidStatusShouldBeOk() throws Exception {

        var supplierId = savedSupplier.getId();

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var updateDto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/suppliers/" + supplierId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id").value(supplierId))
                .andExpect(jsonPath("$.companyName").value(updateDto.getCompanyName()))
                .andExpect(jsonPath("$.contactName").value(updateDto.getContactName()))
                .andExpect(jsonPath("$.contactPhone").value(updateDto.getContactPhone()))
                .andExpect(jsonPath("$.address", Matchers.notNullValue()))
                .andExpect(jsonPath("$.address.country").value(updateDto.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.state").value(updateDto.getAddress().getState()))
                .andExpect(jsonPath("$.address.city").value(updateDto.getAddress().getCity()))
                .andExpect(jsonPath("$.address.zipCode").value(updateDto.getAddress().getZipCode()))
                .andExpect(jsonPath("$.address.street").value(updateDto.getAddress().getStreet()));
    }

    @Test
    public void updateSupplierWhenSupplierExistsAndModelIsInvalidStatusShouldBeBadRequests() throws Exception {
        var supplierId = savedSupplier.getId();

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var updateDto = UpdateSupplierDto.builder()
                .companyName("")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/suppliers/" + supplierId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateSupplierWhenSupplierDoesNotExistsStatusShouldBeNotFound() throws Exception {
        var supplierId = 20000L;

        var address = AddressDto
                .builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var updateDto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(address)
                .build();

        var request = MockMvcRequestBuilders
                .put("/api/suppliers/" + supplierId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
