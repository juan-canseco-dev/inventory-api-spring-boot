package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.security.dtos.users.CreateUserDto;
import com.jcanseco.inventoryapi.security.dtos.users.UpdateUserDto;
import com.jcanseco.inventoryapi.security.dtos.users.UserDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.users.UserDto;
import com.jcanseco.inventoryapi.security.mappers.UserMapper;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.specifications.UserSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final IndexUtility indexUtility;

    public Long createUser(CreateUserDto dto) {
        return 0L;
    }

    public void updateUser(UpdateUserDto dto) {

    }

    public void changeUserRole() {

    }

    public void deleteUser(Long userId) {

    }

    public UserDetailsDto getUserById(Long userId) {
        return null;
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
