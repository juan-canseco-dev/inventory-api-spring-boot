package com.jcanseco.inventoryapi.identity.users.usecases.changerole;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.identity.users.dto.ChangeUserRoleDto;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeUserRoleUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void execute(ChangeUserRoleDto dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", dto.getUserId())));

        if (user.getEmail().equalsIgnoreCase(AuthConstants.ADMIN_EMAIL)) {
            throw new DomainException("Editing the administrator user is not allowed.");
        }

        if (!user.getRole().getId().equals(dto.getRoleId())) {
            var role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new DomainException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));
            user.setRole(role);
            userRepository.saveAndFlush(user);
        }
    }
}

