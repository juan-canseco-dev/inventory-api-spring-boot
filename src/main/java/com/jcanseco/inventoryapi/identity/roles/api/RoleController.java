package com.jcanseco.inventoryapi.identity.roles.api;

import com.jcanseco.inventoryapi.identity.roles.dto.CreateRoleDto;
import com.jcanseco.inventoryapi.identity.roles.dto.GetRolesRequest;
import com.jcanseco.inventoryapi.identity.roles.dto.RoleDetailsDto;
import com.jcanseco.inventoryapi.identity.roles.dto.UpdateRoleDto;
import com.jcanseco.inventoryapi.identity.roles.usecases.create.CreateRoleUseCase;
import com.jcanseco.inventoryapi.identity.roles.usecases.delete.DeleteRoleUseCase;
import com.jcanseco.inventoryapi.identity.roles.usecases.getall.GetRolesUseCase;
import com.jcanseco.inventoryapi.identity.roles.usecases.getbyid.GetRoleByIdUseCase;
import com.jcanseco.inventoryapi.identity.roles.usecases.update.UpdateRoleUseCase;
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
@RequestMapping("api/roles")
@RestController
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final GetRoleByIdUseCase getRoleByIdUseCase;
    private final GetRolesUseCase getRolesUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateRoleDto dto) throws URISyntaxException {
        var roleId = createRoleUseCase.execute(dto);
        var location = new URI("/api/roles/" + roleId);
        return ResponseEntity.created(location).body(roleId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.Update))")
    @PutMapping("{roleId}")
    public ResponseEntity<Void> update(@PathVariable Long roleId, @RequestBody @Valid UpdateRoleDto dto) {
        if (!dto.getRoleId().equals(roleId)) {
            return ResponseEntity.badRequest().build();
        }
        updateRoleUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.Delete))")
    @DeleteMapping("{roleId}")
    public ResponseEntity<Void> delete(@PathVariable Long roleId) {
        deleteRoleUseCase.execute(roleId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.View))")
    @GetMapping("{roleId}")
    public ResponseEntity<RoleDetailsDto> getById(@PathVariable Long roleId) {
        var response = getRoleByIdUseCase.execute(roleId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetRolesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getRolesUseCase.execute(request));
        }
        return ResponseEntity.ok(getRolesUseCase.executePaged(request));
    }
}
