package com.jcanseco.inventoryapi.identity.users.usecases.getbyid;

import com.jcanseco.inventoryapi.identity.users.dto.UserDetailsDto;
import com.jcanseco.inventoryapi.identity.users.mapping.UserMapper;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Transactional(readOnly = true)
    public UserDetailsDto execute(Long userId) {
        return userRepository.findById(userId)
                .map(mapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", userId)));
    }
}

