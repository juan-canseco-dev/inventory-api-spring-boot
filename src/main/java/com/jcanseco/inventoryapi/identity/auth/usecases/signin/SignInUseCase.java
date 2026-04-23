package com.jcanseco.inventoryapi.identity.auth.usecases.signin;

import com.jcanseco.inventoryapi.identity.auth.dto.JwtAuthenticationDto;
import com.jcanseco.inventoryapi.identity.auth.dto.SignInDto;
import com.jcanseco.inventoryapi.identity.auth.security.JwtService;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignInUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public JwtAuthenticationDto execute(SignInDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        return userRepository.findOne(UserSpecifications.byEmail(dto.getEmail()))
                .map(userDetails -> JwtAuthenticationDto.builder()
                        .token(jwtService.generateToken(userDetails))
                        .build())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
    }
}

