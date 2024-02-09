package com.jcanseco.inventoryapi.security.dtos.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtAuthenticationDto {
    private String token;
}
