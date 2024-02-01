package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Map;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ResourceService resourceService;

    private void validatePermissions(List<String> permissions) {
        if (!resourceService.hasPermissions(permissions)) {
            var invalidPermissions = resourceService.getInvalidPermissions(permissions);
            throw new DomainException(
                    String.format("The following permissions are invalid: %s", String.join(", ", invalidPermissions))
            );
        }

        Map<String, List<String>> resourceWithPermissions = resourceService.groupPermissionsWithResource(permissions);

        for (Map.Entry<String, List<String>> entry : resourceWithPermissions.entrySet()) {
            if (!resourceService.hasRequiredPermissionsByResource(entry.getKey(), entry.getValue())) {
                throw new DomainException("The required permissions for the resource are not selected.");
            }
        }
    }

    @Transactional
    public Long createRole(CreateRoleDto dto) {
        validatePermissions(dto.getPermissions());
        var newRole = roleRepository.saveAndFlush(
                Role.builder()
                        .name(dto.getName())
                        .permissions(new HashSet<>(dto.getPermissions()))
                        .build()
        );
        return newRole.getId();
    }

    @Transactional
    public void updateRole(UpdateRoleDto dto) {

        var role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));

        validatePermissions(dto.getPermissions());

        role.setName(dto.getName());
        role.setPermissions(new HashSet<>(dto.getPermissions()));

        roleRepository.saveAndFlush(role);
    }

    @Transactional
    public void deleteRole(Long roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", roleId)));
        roleRepository.delete(role);
    }

    @Transactional(readOnly = true)
    public RoleDetailsDto getRoleById(Long roleId) {
        return null;
    }
}
