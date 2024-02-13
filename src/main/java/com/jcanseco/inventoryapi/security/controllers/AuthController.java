package com.jcanseco.inventoryapi.security.controllers;


import com.jcanseco.inventoryapi.security.dtos.authentication.JwtAuthenticationDto;
import com.jcanseco.inventoryapi.security.dtos.authentication.SignInDto;
import com.jcanseco.inventoryapi.security.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestControllerAdvice
@RequestMapping("api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    @PostMapping("singIn")
    public ResponseEntity<JwtAuthenticationDto> signIn(@RequestBody @Valid SignInDto dto) {
        return ResponseEntity.ok(
                service.signIn(dto)
        );
    }
}
