package com.jcanseco.inventoryapi.identity.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;





@Builder
@Data
@AllArgsConstructor
public class JwtAuthenticationDto {
    private String token;
}








