package com.jcanseco.inventoryapi.identity.roles.dto;

import com.jcanseco.inventoryapi.identity.permissions.domain.Permissions;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;





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








