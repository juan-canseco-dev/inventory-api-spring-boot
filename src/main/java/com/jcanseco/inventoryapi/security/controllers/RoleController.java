package com.jcanseco.inventoryapi.security.controllers;

import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.GetRolesRequest;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
import com.jcanseco.inventoryapi.security.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@RestControllerAdvice
@RequestMapping("api/roles")
@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateRoleDto dto) throws URISyntaxException {
        var roleId = service.createRole(dto);
        var location = new URI("/api/roles/" + roleId);
        return ResponseEntity.created(location).body(roleId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.Update))")
    @PutMapping("{roleId}")
    public ResponseEntity<Void> update(@PathVariable Long roleId, @RequestBody @Valid UpdateRoleDto dto) {
        if (!dto.getRoleId().equals(roleId)) {
            return ResponseEntity.badRequest().build();
        }
        service.updateRole(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.Delete))")
    @DeleteMapping("{roleId}")
    public ResponseEntity<Void> delete(@PathVariable Long roleId) {
        service.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.View))")
    @GetMapping("{roleId}")
    public ResponseEntity<RoleDetailsDto> getById(@PathVariable Long roleId) {
        return ResponseEntity.ok(service.getRoleById(roleId));
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Roles, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetRolesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getRoles(request);
            return ResponseEntity.ok(response);
        }
        var response = service.getRolesPage(request);
        return ResponseEntity.ok(response);
    }
}
