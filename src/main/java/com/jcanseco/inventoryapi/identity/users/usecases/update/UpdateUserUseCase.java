package com.jcanseco.inventoryapi.identity.users.usecases.update;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.users.dto.UpdateUserDto;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(UpdateUserDto dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", dto.getUserId())));

        if (user.getEmail().equals(AuthConstants.ADMIN_EMAIL)) {
            throw new DomainException("Editing the administrator user is not allowed.");
        }

        user.setFullName(dto.getFullName());
        userRepository.saveAndFlush(user);
    }
}

