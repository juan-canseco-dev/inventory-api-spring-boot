package com.jcanseco.inventoryapi.identity.roles.dto;

import java.time.LocalDateTime;
import lombok.*;





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








