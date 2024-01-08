package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDetailsDto;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import com.jcanseco.inventoryapi.entities.CustomerAddress;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerApiIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    private static HttpHeaders httpHeaders;
    private static CustomerAddress defaultAddress;
    private static AddressDto defaultAddressDto;

    @BeforeAll
    public static void setup() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        defaultAddressDto = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        defaultAddress = CustomerAddress.builder()
                .id(1L)
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    @Test
    @Sql(statements = "DELETE FROM customers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCustomerStatusShouldBeCreated() throws JsonProcessingException {
        var dto = CreateCustomerDto.builder()
                .dni("12345678912")
                .fullName("John Doe")
                .phone("555-1234-1")
                .address(defaultAddressDto)
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Test
    @Sql(statements = "INSERT INTO customer_address (id, country, state, city, zip_code, street) VALUES\n" +
                      "(1, 'United States', 'California', 'San Francisco', '94105', '123 Main St');\n" +
                      "INSERT INTO customers (id, dni, phone, full_name, address_id) VALUES\n" +
                      "(1,'123456789', '555-1234-1', 'John Doe', 1)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void updateCustomerStatusShouldBeNoContent() throws JsonProcessingException {

        var customerId = 1L;
        var url = baseUrl() + "/" + customerId;
        var dto = UpdateCustomerDto.builder()
                .customerId(customerId)
                .dni("123456789")
                .fullName("John Doe Smith")
                .phone("555-1234-9")
                .address(defaultAddressDto)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    @Sql(statements = "INSERT INTO customer_address (id, country, state, city, zip_code, street) VALUES\n" +
            "(11, 'United States', 'California', 'San Francisco', '94105', '123 Main St');\n" +
            "INSERT INTO customers (id, dni, phone, full_name, address_id) VALUES\n" +
            "(11,'123456783339', '555-1234-1', 'John Doe', 11)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteCustomerStatusShouldBeNoContent() {
        var customerId = 11L;
        var url = baseUrl() + "/" + customerId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    @Sql(statements = "INSERT INTO customer_address (id, country, state, city, zip_code, street) VALUES\n" +
            "(11, 'United States', 'California', 'San Francisco', '94105', '123 Main St');\n" +
            "INSERT INTO customers (id, dni, phone, full_name, address_id) VALUES\n" +
            "(11,'123456783339', '555-1234-1', 'John Doe', 11)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void getCustomerByIdStatusShouldBeNoContent() {
        var customerId = 11L;
        var url = baseUrl() + "/" + customerId;

        var expected = CustomerDetailsDto.builder()
                .id(customerId)
                .dni("123456783339")
                .phone("555-1234-1")
                .fullName("John Doe")
                .address(
                        AddressDto.builder()
                                .country("United States")
                                .state("California")
                                .city("San Francisco")
                                .zipCode("94105")
                                .street("123 Main St")
                                .build()
                )
                .build();

        var response = restTemplate.exchange(url, HttpMethod.GET, null, CustomerDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var result = response.getBody();
        assertEquals(expected, result);
    }

    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomersStatusShouldBeOk() {

    }

    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomersPageStatusShouldBeOk() {

    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/customers", port);
    }
}
