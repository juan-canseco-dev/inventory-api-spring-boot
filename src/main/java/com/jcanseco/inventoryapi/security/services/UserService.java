package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.dtos.users.*;
import com.jcanseco.inventoryapi.security.entities.User;
import com.jcanseco.inventoryapi.security.mappers.UserMapper;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.specifications.UserSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final IndexUtility indexUtility;
    private final PasswordEncoder passwordEncoder;

    public Long createUser(CreateUserDto dto) {
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

    public void updateUser(UpdateUserDto dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", dto.getUserId())));

        user.setFullName(dto.getFullName());
        userRepository.saveAndFlush(user);
    }

    public void changeUserRole(ChangeUserRoleDto dto) {

        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", dto.getUserId())));

        if (!user.getRole().getId().equals(dto.getRoleId())) {
            var role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new DomainException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));
            user.setRole(role);
            userRepository.saveAndFlush(user);
        }
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", userId)));
        userRepository.delete(user);
    }

    public UserDetailsDto getUserById(Long userId) {
        return  userRepository.findById(userId)
                .map(mapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", userId)));
    }

    public List<UserDto> getUsers() {
        return null;
    }

    public PagedList<UserDto> getUsersPage() {
        return null;
    }

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findOne(UserSpecifications.byEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User : {%s} not found.", username)));
    }

}
