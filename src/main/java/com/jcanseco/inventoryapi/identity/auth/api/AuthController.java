package com.jcanseco.inventoryapi.identity.auth.api;

import com.jcanseco.inventoryapi.identity.auth.dto.JwtAuthenticationDto;
import com.jcanseco.inventoryapi.identity.auth.dto.SignInDto;
import com.jcanseco.inventoryapi.identity.auth.usecases.signin.SignInUseCase;
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

    private final SignInUseCase signInUseCase;

    @PostMapping("singIn")
    public ResponseEntity<JwtAuthenticationDto> signIn(@RequestBody @Valid SignInDto dto) {
        var response = signInUseCase.execute(dto);
        return ResponseEntity.ok(response);
    }
}
