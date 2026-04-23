package com.jcanseco.inventoryapi.identity.roles.usecases.delete;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteRoleUseCase {

    private final RoleRepository roleRepository;

    @Transactional
    public void execute(Long roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", roleId)));

        if (role.getName().equalsIgnoreCase(AuthConstants.ADMIN_ROLE_NAME)) {
            throw new DomainException("Deleting the administrator role is not allowed.");
        }

        roleRepository.delete(role);
    }
}

