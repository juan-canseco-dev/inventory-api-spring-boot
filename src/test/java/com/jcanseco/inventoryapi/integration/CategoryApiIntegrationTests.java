package com.jcanseco.inventoryapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
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
public class CategoryApiIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private static HttpHeaders httpHeaders;
    @BeforeAll
    public static void setup() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Sql("/multiple-categories.sql")
    public void createCategoryStatusShouldBeCreated() throws JsonProcessingException {

        var dto = CreateCategoryDto.builder()
                .name("New Category")
                .build();
        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0L);
    }

    @Test
    @Sql("/multiple-categories.sql")
    public void updateCategoryStatusShouldBeNoContent() throws JsonProcessingException {

        var categoryId = 1L;
        var url = baseUrl() + "/" + categoryId;
        var dto = UpdateCategoryDto.builder()
                .categoryId(categoryId)
                .name("Computers & Electronics")
                .build();

        var entity = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    @Sql("/multiple-categories.sql")
    public void deleteCategoryStatusShouldBeNoContent() {
        var categoryId = 1L;
        var url = baseUrl() + "/" + categoryId;
        var response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Sql("/multiple-categories.sql")
    @Test
    public void getCategoryByIdStatusShouldBeOk() {
        var categoryId = 1L;
        var expectedCategory = CategoryDto.builder()
                .id(categoryId)
                .name("Electronics")
                .build();

        var url = baseUrl() + "/" + categoryId;
        var response = restTemplate.exchange(url, HttpMethod.GET, null, CategoryDto.class);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var category = response.getBody();
        assertEquals(expectedCategory, category);
    }

    @Sql("/multiple-categories.sql")
    @Test
    public void getCategoriesStatusShouldBeOk() {
        var url = baseUrl() + "?orderBy=name&sortOrder=asc&name=a";
        var responseType = new ParameterizedTypeReference<List<CategoryDto>>() {};
        var response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var categories = response.getBody();
        assertEquals(5, categories.size());
    }

    @Sql("/multiple-categories.sql")
    @Test
    public void getCategoriesPageStatusShouldBeOk() {
        var url = baseUrl() + "?pageNumber=1&pageSize=2&orderBy=name&sortOrder=asc&name=a";
        var responseType = new ParameterizedTypeReference<PagedList<CategoryDto>>() {};
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
        return String.format("http://localhost:%d/api/categories", port);
    }
}
