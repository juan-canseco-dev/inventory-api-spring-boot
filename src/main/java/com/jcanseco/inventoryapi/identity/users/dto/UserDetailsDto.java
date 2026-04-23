package com.jcanseco.inventoryapi.identity.users.dto;

import com.jcanseco.inventoryapi.identity.roles.dto.RoleDetailsDto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;





@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {
    private Long id;
    private RoleDetailsDto role;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}








