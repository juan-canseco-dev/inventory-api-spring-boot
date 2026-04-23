package com.jcanseco.inventoryapi.identity.auth.security;

import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmailWithRoleAndPermissions(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User : {%s} not found.", username)));
    }
}
