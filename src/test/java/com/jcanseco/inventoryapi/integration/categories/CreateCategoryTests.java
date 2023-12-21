package com.jcanseco.inventoryapi.integration.categories;

import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateCategoryTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CategoryRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void createCategoryWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var createdDto = new CreateCategoryDto("Electronics");

        var request = MockMvcRequestBuilders
                .post("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createdDto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void createCategoryWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception{
        var createdDto = new CreateCategoryDto("");

        var request = MockMvcRequestBuilders
                .post("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createdDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}
