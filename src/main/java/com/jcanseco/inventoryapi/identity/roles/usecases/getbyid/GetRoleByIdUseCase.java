package com.jcanseco.inventoryapi.identity.roles.usecases.getbyid;

import com.jcanseco.inventoryapi.identity.roles.dto.RoleDetailsDto;
import com.jcanseco.inventoryapi.identity.roles.mapping.RoleMapper;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRoleByIdUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    @Transactional(readOnly = true)
    public RoleDetailsDto execute(Long roleId) {
        return roleRepository.findById(roleId)
                .map(mapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", roleId)));
    }
}

