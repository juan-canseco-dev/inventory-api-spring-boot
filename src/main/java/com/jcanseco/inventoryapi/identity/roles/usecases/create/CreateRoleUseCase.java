package com.jcanseco.inventoryapi.identity.roles.usecases.create;

import com.jcanseco.inventoryapi.identity.permissions.domain.PermissionCatalog;
import com.jcanseco.inventoryapi.identity.roles.dto.CreateRoleDto;
import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import java.util.HashSet;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionCatalog permissionCatalog;

    @Transactional
    public Long execute(CreateRoleDto dto) {
        if (roleRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DomainException(String.format("The role with name %s already exists.", dto.getName()));
        }

        validatePermissions(dto.getPermissions());

        var newRole = roleRepository.saveAndFlush(
                Role.builder()
                        .name(dto.getName())
                        .permissions(new HashSet<>(dto.getPermissions()))
                        .build()
        );
        return newRole.getId();
    }

    private void validatePermissions(java.util.List<String> permissions) {
        if (!permissionCatalog.hasPermissions(permissions)) {
            var invalidPermissions = permissionCatalog.getInvalidPermissions(permissions);
            throw new DomainException(
                    String.format("The following permissions are invalid: %s", String.join(", ", invalidPermissions))
            );
        }

        Map<String, java.util.List<String>> resourceWithPermissions =
                permissionCatalog.groupPermissionsWithResource(permissions);

        for (Map.Entry<String, java.util.List<String>> entry : resourceWithPermissions.entrySet()) {
            if (!permissionCatalog.hasRequiredPermissionsByResource(entry.getKey(), entry.getValue())) {
                throw new DomainException("The required permissions for the resource are not selected.");
            }
        }
    }
}

