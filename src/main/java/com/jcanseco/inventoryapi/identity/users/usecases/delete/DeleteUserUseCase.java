package com.jcanseco.inventoryapi.identity.users.usecases.delete;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", userId)));

        if (user.getEmail().equalsIgnoreCase(AuthConstants.ADMIN_EMAIL)) {
            throw new DomainException("Deleting the administrator user is not allowed.");
        }

        userRepository.delete(user);
    }
}

