package com.jcanseco.inventoryapi.security.dtos.roles;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDetailsDto {
    private Long id;
    private String name;
    private List<String> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
