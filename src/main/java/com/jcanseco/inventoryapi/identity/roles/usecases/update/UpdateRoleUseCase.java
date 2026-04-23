package com.jcanseco.inventoryapi.identity.roles.usecases.update;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.permissions.domain.PermissionCatalog;
import com.jcanseco.inventoryapi.identity.roles.dto.UpdateRoleDto;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import java.util.HashSet;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionCatalog permissionCatalog;

    @Transactional
    public void execute(UpdateRoleDto dto) {
        var role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));

        if (role.getName().equalsIgnoreCase(AuthConstants.ADMIN_ROLE_NAME)) {
            throw new DomainException("Editing the administrator role is not allowed.");
        }

        if (!role.getName().equalsIgnoreCase(dto.getName()) && roleRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DomainException(String.format("The role with name %s already exists.", dto.getName()));
        }

        validatePermissions(dto.getPermissions());

        role.setName(dto.getName());
        role.setPermissions(new HashSet<>(dto.getPermissions()));
        roleRepository.saveAndFlush(role);
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

