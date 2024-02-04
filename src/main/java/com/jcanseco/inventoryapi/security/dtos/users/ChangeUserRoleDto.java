package com.jcanseco.inventoryapi.security.dtos.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserRoleDto {
    @Min(1)
    @NotNull
    private Long userId;

    @Min(1)
    @NotNull
    private Long roleId;
}
