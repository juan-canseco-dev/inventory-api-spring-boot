package com.jcanseco.inventoryapi.security.dtos.authentication;

import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
public class SignInDto {
    @Size(max = 50)
    @NotBlank
    @NotEmpty
    @Email
    private String email;

    @Size(max = 30)
    @NotBlank
    @NotEmpty
    private String password;
}
