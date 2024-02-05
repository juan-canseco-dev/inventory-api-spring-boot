package com.jcanseco.inventoryapi.controller;

import com.jcanseco.inventoryapi.controllers.UnitOfMeasurementController;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
import com.jcanseco.inventoryapi.security.services.UserService;
import com.jcanseco.inventoryapi.services.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@MockBeans({
        @MockBean(CategoryService.class),
        @MockBean(CustomerService.class),
        @MockBean(ProductService.class),
        @MockBean(SupplierService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(ResourceService.class),
        @MockBean(RoleService.class),
        @MockBean(UserService.class)
})
@WebMvcTest(
        controllers = UnitOfMeasurementController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class UnitOfMeasurementControllerUnitTests {

    @MockBean
    private UnitService unitService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createUnitWhenModelIsValidStatusShouldBeCreated() throws Exception {

        var dto = CreateUnitOfMeasurementDto.builder()
                .name("New Unit")
                .build();

        when(unitService.createUnit(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    public void createUnitWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var dto = CreateUnitOfMeasurementDto.builder()
                .name("")
                .build();

        when(unitService.createUnit(dto)).thenReturn(1L);

        mockMvc.perform(
                        post("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUnitWhenModelIsValidStatusShouldBeNoContent() throws Exception {

        var unitId = 1L;

        var updateUnitDto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Piece")
                .build();

        doNothing().when(unitService).updateUnit(updateUnitDto);

        mockMvc.perform(
                        put("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUnitDto))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateUnitWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {
        var unitId = 1L;

        var updateUnitDto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name(" ")
                .build();

        doNothing().when(unitService).updateUnit(updateUnitDto);

        mockMvc.perform(
                        put("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUnitDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUnitWhenModelAndPathIdAreDifferentStatusShouldBeBadRequest() throws Exception {
        var unitId = 1L;

        var updateUnitDto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Each")
                .build();

        doNothing().when(unitService).updateUnit(updateUnitDto);

        mockMvc.perform(
                        put("/api/units/5")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUnitDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUnitWhenUnitDoNotExistsStatusShouldBeNotFound() throws Exception {

        var unitId = 1L;

        var updateUnitDto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Piece")
                .build();

        doThrow(new NotFoundException(String.format("Unit Of Measurement with the Id {%d} was not found.", unitId)))
                .when(unitService)
                .updateUnit(updateUnitDto);

        mockMvc.perform(
                        put("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateUnitDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUnitWhenUnitExistsStatusShouldBeNoContent() throws Exception {
        var unitId = 1L;
        doNothing().when(unitService).deleteUnit(unitId);
        mockMvc.perform(
                        delete("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUnitWhenUnitDoNotExistsStatusShouldBeNotFound() throws Exception {
        var unitId = 1L;
        doThrow(new NotFoundException(String.format("Unit Of Measurement with the Id {%d} was not found.", unitId)))
                .when(unitService)
                .deleteUnit(unitId);
        mockMvc.perform(
                        delete("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUnitByIdWhenUnitExistsStatusShouldBeOk() throws Exception {
        var unitId = 1L;
        var unitDto = UnitOfMeasurementDto.builder()
                .id(unitId)
                .name("Piece")
                .build();
        when(unitService.getUnitById(unitId)).thenReturn(unitDto);
        mockMvc.perform(
                        get("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUnitByIdWhenUnitDoNotExistsStatusShouldBeNotFound() throws Exception {
        var unitId = 1L;
        doThrow(new NotFoundException(String.format("Unit Of Measurement with the Id {%d} was not found.", unitId)))
                .when(unitService).getUnitById(unitId);
        mockMvc.perform(
                        get("/api/units/" + unitId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUnitsWhenSortOrderIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sortOrder", "invalid_sort_order")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUnitsWhenOrderByIsInvalidStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("orderBy", "invalid_order_by")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUnitsPageWhenPageNumberOrPageSizeAreLessThanOneStatusShouldBeBadRequest() throws Exception {
        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", "0")
                                .param("pageSize", "-1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUnitsWhenPageSizeAndPageNumberAreNotPresentStatusShouldBeOk() throws Exception {


        var getUnitsRequest = GetUnitsOfMeasurementRequest.builder()
                .build();

        var units = List.of(
                UnitOfMeasurementDto.builder().id(1L).name("Piece").build(),
                UnitOfMeasurementDto.builder().id(2L).name("Each").build()
        );

        when(unitService.getUnits(getUnitsRequest)).thenReturn(units);

        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getUnitsPageWhenPageSizeAndPageNumberArePresentStatusShouldBeOk() throws Exception {

        var getUnitsPageRequest = GetUnitsOfMeasurementRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        var units = List.of(
                UnitOfMeasurementDto.builder().id(1L).name("Electronics").build()
        );

        var pagedList = new PagedList<>(units, 1, 1, 2, 2);

        when(unitService.getUnitsPage(getUnitsPageRequest)).thenReturn(pagedList);

        mockMvc.perform(
                        get("/api/units")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("pageNumber", getUnitsPageRequest.getPageNumber().toString())
                                .param("pageSize", getUnitsPageRequest.getPageSize().toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.items", Matchers.notNullValue()))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.hasPreviousPage").value(false))
                .andExpect(jsonPath("$.hasNextPage").value(true));
    }

}