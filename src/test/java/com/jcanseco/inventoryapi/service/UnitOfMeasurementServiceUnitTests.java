package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.entities.UnitOfMeasurement;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.UnitOfMeasurementMapper;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.services.UnitService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UnitOfMeasurementServiceUnitTests {
    @Mock
    private UnitOfMeasurementRepository unitRepository;
    @Spy
    private UnitOfMeasurementMapper unitMapper = Mappers.getMapper(UnitOfMeasurementMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @InjectMocks
    private UnitService unitService;
    private List<UnitOfMeasurement> units;
    @BeforeEach
    public void setup() {
        units = List.of(
                UnitOfMeasurement.builder().id(1L).name("Each").build(),
                UnitOfMeasurement.builder().id(2L).name("Piece").build()
        );
    }

    @Test
    public void createUnitCreateShouldBeSuccessful() {

        var createdUnitId = 1L;

        var dto = CreateUnitOfMeasurementDto.builder()
                .name("New Unit")
                .build();

        var mappedUnit = UnitOfMeasurement.builder()
                .name("New Unit")
                .build();

        var newUnit = UnitOfMeasurement.builder()
                .id(createdUnitId)
                .name("New Unit")
                .build();

        when(unitMapper.createDtoToEntity(dto)).thenReturn(mappedUnit);
        when(unitRepository.saveAndFlush(mappedUnit)).thenReturn(newUnit);

        var resultUnitId = unitService.createUnit(dto);
        assertEquals(createdUnitId, resultUnitId);
    }

    @Test
    public void updateUnitWhenUnitExistsUpdateShouldBeSuccessful() {
        var unitId = 1L;

        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Updated Unit")
                .build();

        var foundUnit = UnitOfMeasurement.builder()
                .id(unitId)
                .name("New Unit")
                .build();

        when(unitRepository.findById(unitId)).thenReturn(Optional.of(foundUnit));
        unitService.updateUnit(dto);

        verify(unitRepository, times(1)).saveAndFlush(foundUnit);

        var unitArgCaptor = ArgumentCaptor.forClass(UnitOfMeasurement.class);

        verify(unitRepository).saveAndFlush(unitArgCaptor.capture());
        var updatedUnit = unitArgCaptor.getValue();
        assertNotNull(updatedUnit);
        assertEquals(unitId, updatedUnit.getId());
        assertEquals(dto.getName(), updatedUnit.getName());
    }

    @Test
    public void updateUnitWhenUnitDoNotExistsShouldThrowNotFoundException() {
        var unitId = 1L;

        var dto = UpdateUnitOfMeasurementDto.builder()
                .unitOfMeasurementId(unitId)
                .name("Updated Unit Name")
                .build();

        when(unitRepository.findById(unitId)).thenThrow(new NotFoundException("Unit Not Found"));
        assertThrows(NotFoundException.class, () -> unitService.updateUnit(dto));
    }

    @Test
    public void deleteUnitWhenUnitExistsDeleteShouldBeSuccessful() {

        var unitId = 1L;
        var foundUnit = UnitOfMeasurement
                .builder()
                .id(unitId)
                .name("Found Unit")
                .build();

        when(unitRepository.findById(unitId)).thenReturn(Optional.of(foundUnit));
        doNothing().when(unitRepository).delete(foundUnit);
        unitService.deleteUnit(unitId);
        verify(unitRepository, times(1)).delete(foundUnit);
    }

    @Test
    public void deleteUnitWhenUnitDoNotExistsShouldThrowNotFoundException() {
        var unitId = 1L;
        when(unitRepository.findById(unitId)).thenThrow(new NotFoundException("Unit Not Found"));
        assertThrows(NotFoundException.class, () -> unitService.deleteUnit(unitId));
    }

    @Test
    public void getUnitWhenUnitExistsGetShouldBeSuccessful() {

        var unitId = 1L;

        var foundUnit = UnitOfMeasurement.builder()
                .id(unitId)
                .name("Found Unit")
                .build();

        var unitDto = UnitOfMeasurementDto.builder()
                .id(unitId)
                .name("Found Unit")
                .build();

        when(unitRepository.findById(unitId)).thenReturn(Optional.of(foundUnit));
        when(unitMapper.entityToDto(foundUnit)).thenReturn(unitDto);

        var resultDto = unitService.getUnitById(unitId);
        assertEquals(unitDto, resultDto);
    }

    @Test
    public void getUnitWhenUnitDoNotExistsShouldThrowNotFoundException() {
        var unitId = 1L;
        when(unitRepository.findById(unitId)).thenThrow(new NotFoundException("Unit Not Found"));
        assertThrows(NotFoundException.class, () -> unitService.getUnitById(unitId));
    }

    @Test
    public void getUnitsShouldReturnList() {

        var expectedResult = units.stream()
                .map(unitMapper::entityToDto)
                .toList();

        var request = GetUnitsOfMeasurementRequest.builder().build();
        Specification<UnitOfMeasurement> mockSpec = any();
        when(unitRepository.findAll(mockSpec)).thenReturn(units);
        var result = unitService.getUnits(request);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertThat(result).hasSameElementsAs(expectedResult);
    }

    @Test
    public void getUnitsPageShouldReturnPagedList() {

        var totalUnitsInDb = 4;

        var totalPages = 2;

        var expectedItems = units.stream()
                .map(unitMapper::entityToDto)
                .toList();


        var request = GetUnitsOfMeasurementRequest.builder()
                .pageNumber(1)
                .pageSize(2)
                .build();

        Specification<UnitOfMeasurement> mockSpec = any();
        PageRequest mockPageRequest = any();
        Page<UnitOfMeasurement> mockPage = new PageImpl<>(
                units,
                Pageable.ofSize(2),
                totalUnitsInDb
        );

        when(unitRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = unitService.getUnitsPage(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalUnitsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
