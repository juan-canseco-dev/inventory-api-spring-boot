package com.jcanseco.inventoryapi.identity.users.usecases.create;

import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.identity.users.dto.CreateUserDto;
import com.jcanseco.inventoryapi.identity.users.domain.User;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long execute(CreateUserDto dto) {
        var role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new DomainException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));

        var newUser = userRepository.saveAndFlush(
                User.builder()
                        .role(role)
                        .email(dto.getEmail())
                        .fullName(dto.getFullName())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .build()
        );

        return newUser.getId();
    }
}

