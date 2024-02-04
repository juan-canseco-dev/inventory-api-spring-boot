package com.jcanseco.inventoryapi.security.dtos.users;

import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {
    private String id;
    private RoleDetailsDto role;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
