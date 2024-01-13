package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.AddressDto;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.suppliers.CreateSupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDetailsDto;
import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import com.jcanseco.inventoryapi.dtos.suppliers.UpdateSupplierDto;
import com.jcanseco.inventoryapi.entities.Address;
import org.junit.jupiter.api.BeforeAll;
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
public class SupplierApiIntegrationTests {

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

    @Sql(statements = "DELETE FROM suppliers;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createSupplierStatusShouldBeCreated() throws JsonProcessingException {
        var dto = CreateSupplierDto.builder()
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
                .address(defaultAddressDto)
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Sql(
            statements = "INSERT INTO suppliers (id, company_name, contact_name, contact_phone, " +
                    "supplier_address_country, supplier_address_state, supplier_address_city, " +
                    "supplier_address_zip_code, supplier_address_street) " +
                    "VALUES (11, 'ABC Corp', 'John Doe', '555-1234-1', 'United States', " +
                    "'California', 'San Francisco', '94105', '123 Main St')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(statements = "DELETE FROM suppliers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void updateSupplierStatusShouldBeNoContent() throws JsonProcessingException {

        var supplierId = 11L;
        var url = baseUrl() + "/" +supplierId;

        var address = AddressDto.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();

        var dto = UpdateSupplierDto.builder()
                .supplierId(supplierId)
                .companyName("ABC Corporation")
                .contactName("John Doe Smith")
                .contactPhone("555-1234-7")
                .address(address)
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql(
            statements = "INSERT INTO suppliers (id, company_name, contact_name, contact_phone, " +
                    "supplier_address_country, supplier_address_state, supplier_address_city, " +
                    "supplier_address_zip_code, supplier_address_street) " +
                    "VALUES (12, 'ABC Corp', 'John Doe', '555-1234-1', 'United States', " +
                    "'California', 'San Francisco', '94105', '123 Main St')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(statements = "DELETE FROM suppliers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void deleteSupplierStatusShouldBeNoContent() {
        var supplierId = 12L;
        var url = baseUrl() + "/" +supplierId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }


    @Sql(
            statements = "INSERT INTO suppliers (id, company_name, contact_name, contact_phone, " +
                    "supplier_address_country, supplier_address_state, supplier_address_city, " +
                    "supplier_address_zip_code, supplier_address_street) " +
                    "VALUES (13, 'ABC Corp', 'John Doe', '555-1234-1', 'United States', " +
                    "'California', 'San Francisco', '94105', '123 Main St')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(statements = "DELETE FROM suppliers", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getSupplierByIdStatusShouldBeOk() {

        var supplierId = 13L;
        var url = baseUrl() + "/" + supplierId;

        var expected = SupplierDetailsDto.builder()
                .id(supplierId)
                .companyName("ABC Corp")
                .contactName("John Doe")
                .contactPhone("555-1234-1")
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
        var response = restTemplate.exchange(url, HttpMethod.GET, null,SupplierDetailsDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var result = response.getBody();
        assertEquals(expected, result);
    }

    @Sql(value = "/multiple-suppliers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM suppliers;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getSuppliersStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=companyName&sortOrder=asc&companyName=a&contactName=l&contactPhone=5";
        var responseType = new ParameterizedTypeReference<List<SupplierDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var suppliers = response.getBody();
        assertEquals(3, suppliers.size());

    }

    @Sql(value = "/multiple-suppliers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM suppliers;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getSuppliersPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=companyName&sortOrder=asc&companyName=a&contactName=l&contactPhone=5";
        var responseType = new ParameterizedTypeReference<PagedList<SupplierDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var pagedList = response.getBody();
        assertEquals(1, pagedList.getPageNumber());
        assertEquals(2, pagedList.getPageSize());
        assertEquals(2, pagedList.getTotalPages());
        assertEquals(3, pagedList.getTotalElements());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/suppliers", port);
    }
}
