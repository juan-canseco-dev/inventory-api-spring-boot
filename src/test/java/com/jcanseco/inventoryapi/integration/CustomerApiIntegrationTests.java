package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.customers.CreateCustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDetailsDto;
import com.jcanseco.inventoryapi.dtos.customers.CustomerDto;
import com.jcanseco.inventoryapi.dtos.customers.UpdateCustomerDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class CustomerApiIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    private static HttpHeaders httpHeaders;
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
    }


    @Sql("/multiple-customers.sql")
    @Test
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

    @Sql("/multiple-customers.sql")
    @Test
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


    @Sql("/multiple-customers.sql")
    @Test
    public void deleteCustomerStatusShouldBeNoContent()  {
        var customerId = 1L;
        var url = baseUrl() + "/" + customerId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomerByIdStatusShouldBeNoContent() {
        var customerId = 1L;

        var url = baseUrl() + "/" + customerId;

        var expected = CustomerDetailsDto.builder()
                .id(customerId)
                .dni("123456789")
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
        var url = baseUrl() + "?orderBy=fullName&sortOrder=asc&dni=1&phone=555&fullName=o";
        var responseType = new ParameterizedTypeReference<List<CustomerDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var customers = response.getBody();
        assertEquals(5, customers.size());
    }

    @Sql("/multiple-customers.sql")
    @Test
    public void getCustomersPageShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=fullName&sortOrder=asc&dni=1&phone=555&fullName=o";
        var responseType = new ParameterizedTypeReference<PagedList<CustomerDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(2, pagedList.getPageSize());
        assertEquals(3, pagedList.getTotalPages());
        assertEquals(5, pagedList.getTotalElements());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/customers", port);
    }
}
