package com.jcanseco.inventoryapi.catalog.units.api;

import com.jcanseco.inventoryapi.catalog.units.dto.CreateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.dto.GetUnitsOfMeasurementRequest;
import com.jcanseco.inventoryapi.catalog.units.dto.UnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.dto.UpdateUnitOfMeasurementDto;
import com.jcanseco.inventoryapi.catalog.units.usecases.create.CreateUnitUseCase;
import com.jcanseco.inventoryapi.catalog.units.usecases.delete.DeleteUnitUseCase;
import com.jcanseco.inventoryapi.catalog.units.usecases.getall.GetUnitsUseCase;
import com.jcanseco.inventoryapi.catalog.units.usecases.getbyid.GetUnitByIdUseCase;
import com.jcanseco.inventoryapi.catalog.units.usecases.update.UpdateUnitUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestControllerAdvice
@RequestMapping("api/units")
@RestController
@RequiredArgsConstructor
public class UnitOfMeasurementController {

    private final CreateUnitUseCase createUnitUseCase;
    private final UpdateUnitUseCase updateUnitUseCase;
    private final DeleteUnitUseCase deleteUnitUseCase;
    private final GetUnitByIdUseCase getUnitByIdUseCase;
    private final GetUnitsUseCase getUnitsUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.UnitsOfMeasurement, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateUnitOfMeasurementDto dto) throws URISyntaxException {
        var unitId = createUnitUseCase.execute(dto);
        var location = new URI("/api/units/" + unitId);
        return ResponseEntity.created(location).body(unitId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.UnitsOfMeasurement, @Action.Update))")
    @PutMapping("{unitId}")
    public ResponseEntity<Void> update(@PathVariable Long unitId, @RequestBody @Valid UpdateUnitOfMeasurementDto dto) {
        if (!dto.getUnitOfMeasurementId().equals(unitId)) {
            return ResponseEntity.badRequest().build();
        }
        updateUnitUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.UnitsOfMeasurement, @Action.Delete))")
    @DeleteMapping("{unitId}")
    public ResponseEntity<Void> delete(@PathVariable Long unitId) {
        deleteUnitUseCase.execute(unitId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.UnitsOfMeasurement, @Action.View))")
    @GetMapping("{unitId}")
    public ResponseEntity<UnitOfMeasurementDto> getById(@PathVariable Long unitId) {
        var response = getUnitByIdUseCase.execute(unitId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.UnitsOfMeasurement, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetUnitsOfMeasurementRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getUnitsUseCase.execute(request));
        }
        return ResponseEntity.ok(getUnitsUseCase.executePaged(request));
    }
}
