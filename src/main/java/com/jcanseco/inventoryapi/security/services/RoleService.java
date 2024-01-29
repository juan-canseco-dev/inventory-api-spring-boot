package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Long createRole(CreateRoleDto dto) {
        return null;
    }

    public void updateRole(UpdateRoleDto dto) {

    }

    public void deleteRole(Long roleId) {

    }



}
