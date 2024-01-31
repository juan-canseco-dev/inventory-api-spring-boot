package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.resources.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ResourceService resourceService;

    // TODO: Implement annotation validation named all permissions exists
    // TODO: Create validation for required permissions

    public Long createRole(CreateRoleDto dto) {

        var allPermissions = resourceService.allPermissions();

        if (!allPermissions.containsAll(dto.getPermissions())) {

            var invalidPermissions = dto.getPermissions()
                    .stream().filter(p -> !allPermissions.contains(p))
                    .toList();

            throw new DomainException(
                    String.format("The following permissions are invalid : %s", String.join(", ", invalidPermissions))
            );
        }

        return 0L;
    }

    public void updateRole(UpdateRoleDto dto) {

    }

    public void deleteRole(Long roleId) {

    }
}
