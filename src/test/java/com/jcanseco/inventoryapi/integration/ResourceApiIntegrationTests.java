package com.jcanseco.inventoryapi.integration;

import com.jcanseco.inventoryapi.security.resources.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class ResourceApiIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getResourcesStatusShouldBeOk() {
        var responseType = new ParameterizedTypeReference<List<Resource>>() {};
        var response = restTemplate.exchange(baseUrl(), HttpMethod.GET, null, responseType);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        var resources = response.getBody();
        assertEquals(9, resources.size());
    }

    private String baseUrl() {
        return String.format("http://localhost:%d/api/resources", port);
    }
}
