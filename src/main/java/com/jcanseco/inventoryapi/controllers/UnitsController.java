package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.units.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.dtos.units.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.dtos.units.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.services.UnitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/units")
@RestController
public class UnitsController {

    private final UnitService service;

    @PostMapping
    public ResponseEntity<UnitOfMeasurementDto> create(@RequestBody @Valid CreateUnitOfMeasurementDto dto) {
        return ResponseEntity.ok(service.createUnit(dto));
    }

    @PutMapping("{unitId}")
    public ResponseEntity<UnitOfMeasurementDto> update(@PathVariable Long unitId, @RequestBody @Valid UpdateUnitOfMeasurementDto dto) {
        if (!dto.getUnitOfMeasurementId().equals(unitId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.updateUnit(dto));
    }

    @DeleteMapping("{unitId}")
    public ResponseEntity<?> delete(@PathVariable Long unitId) {
        service.deleteUnit(unitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{unitId}")
    public ResponseEntity<UnitOfMeasurementDto> getById(@PathVariable Long unitId) {
        return ResponseEntity.ok(service.getUnitById(unitId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetUnitsOfMeasurementRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getUnits(request.getName());
            return ResponseEntity.ok(response);
        }
        var response = service.getUnitsPage(request);
        return ResponseEntity.ok(response);
    }
}
