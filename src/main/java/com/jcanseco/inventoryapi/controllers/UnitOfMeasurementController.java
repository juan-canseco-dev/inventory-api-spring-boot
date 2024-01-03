package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.services.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@RestControllerAdvice
@RequestMapping("api/units")
@RestController
@RequiredArgsConstructor
public class UnitOfMeasurementController {

    private final UnitService unitService;
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateUnitOfMeasurementDto dto) throws URISyntaxException {
        var unitId = unitService.createUnit(dto);
        var location = new URI("/api/units/" + unitId);
        return ResponseEntity.created(location).body(unitId);
    }

    @PutMapping("{unitId}")
    public ResponseEntity<UnitOfMeasurementDto> update(@PathVariable Long unitId, @RequestBody @Valid UpdateUnitOfMeasurementDto dto) {
        if (!dto.getUnitOfMeasurementId().equals(unitId)) {
            return ResponseEntity.badRequest().build();
        }
        unitService.updateUnit(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{unitId}")
    public ResponseEntity<?> delete(@PathVariable Long unitId) {
        unitService.deleteUnit(unitId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{unitId}")
    public ResponseEntity<UnitOfMeasurementDto> getById(@PathVariable Long unitId) {
        return ResponseEntity.ok(unitService.getUnitById(unitId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetUnitsOfMeasurementRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = unitService.getUnits(request);
            return ResponseEntity.ok(response);
        }
        var response = unitService.getUnitsPage(request);
        return ResponseEntity.ok(response);
    }
}
