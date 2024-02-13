package com.jcanseco.inventoryapi.security.controllers;

import com.jcanseco.inventoryapi.security.dtos.users.*;
import com.jcanseco.inventoryapi.security.services.UserService;
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
@RequestMapping("api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateUserDto dto) throws URISyntaxException {
        var userId = service.createUser(dto);
        var location = new URI("/api/users/" + userId);
        return ResponseEntity.created(location).body(userId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.Update))")
    @PutMapping("{userId}")
    public ResponseEntity<Void> update(@PathVariable Long userId, @RequestBody @Valid UpdateUserDto dto) {
        if (!dto.getUserId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        service.updateUser(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.ChangeRole))")
    @PutMapping("{userId}/changeRole")
    public ResponseEntity<Void> changeRole(@PathVariable Long userId, @RequestBody @Valid ChangeUserRoleDto dto) {
        if (!dto.getUserId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        service.changeUserRole(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.Delete))")
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.View))")
    @GetMapping("{userId}")
    public ResponseEntity<UserDetailsDto> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetUsersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getUsers(request);
            return ResponseEntity.ok(response);
        }
        var response = service.getUsersPage(request);
        return ResponseEntity.ok(response);
    }
}
