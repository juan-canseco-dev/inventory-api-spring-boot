package com.jcanseco.inventoryapi.security.dtos.roles;

import lombok.*;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
