package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.security.dtos.authentication.JwtAuthenticationDto;
import com.jcanseco.inventoryapi.security.dtos.authentication.SignInDto;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.specifications.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private JwtAuthenticationDto userToToken(UserDetails userDetails) {
        return JwtAuthenticationDto.builder()
                .token(jwtService.generateToken(userDetails))
                .build();
    }

    @Transactional
    public JwtAuthenticationDto signIn(SignInDto dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        return userRepository.findOne(UserSpecifications.byEmail(dto.getEmail()))
                .map(this::userToToken)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
    }
}
