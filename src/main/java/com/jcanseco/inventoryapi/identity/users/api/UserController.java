package com.jcanseco.inventoryapi.identity.users.api;

import com.jcanseco.inventoryapi.identity.users.dto.ChangeUserRoleDto;
import com.jcanseco.inventoryapi.identity.users.dto.CreateUserDto;
import com.jcanseco.inventoryapi.identity.users.dto.GetUsersRequest;
import com.jcanseco.inventoryapi.identity.users.dto.UpdateUserDto;
import com.jcanseco.inventoryapi.identity.users.dto.UserDetailsDto;
import com.jcanseco.inventoryapi.identity.users.usecases.changerole.ChangeUserRoleUseCase;
import com.jcanseco.inventoryapi.identity.users.usecases.create.CreateUserUseCase;
import com.jcanseco.inventoryapi.identity.users.usecases.delete.DeleteUserUseCase;
import com.jcanseco.inventoryapi.identity.users.usecases.getall.GetUsersUseCase;
import com.jcanseco.inventoryapi.identity.users.usecases.getbyid.GetUserByIdUseCase;
import com.jcanseco.inventoryapi.identity.users.usecases.update.UpdateUserUseCase;
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
@RequestMapping("api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUsersUseCase getUsersUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateUserDto dto) throws URISyntaxException {
        var userId = createUserUseCase.execute(dto);
        var location = new URI("/api/users/" + userId);
        return ResponseEntity.created(location).body(userId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.Update))")
    @PutMapping("{userId}")
    public ResponseEntity<Void> update(@PathVariable Long userId, @RequestBody @Valid UpdateUserDto dto) {
        if (!dto.getUserId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        updateUserUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.ChangeRole))")
    @PutMapping("{userId}/changeRole")
    public ResponseEntity<Void> changeRole(@PathVariable Long userId, @RequestBody @Valid ChangeUserRoleDto dto) {
        if (!dto.getUserId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        changeUserRoleUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.Delete))")
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        deleteUserUseCase.execute(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.View))")
    @GetMapping("{userId}")
    public ResponseEntity<UserDetailsDto> getById(@PathVariable Long userId) {
        var response = getUserByIdUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Users, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetUsersRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getUsersUseCase.execute(request));
        }
        return ResponseEntity.ok(getUsersUseCase.executePaged(request));
    }
}
