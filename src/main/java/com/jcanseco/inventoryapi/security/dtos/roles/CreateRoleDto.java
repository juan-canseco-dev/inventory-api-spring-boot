package com.jcanseco.inventoryapi.security.dtos.roles;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleDto {
    @Size(max = 50)
    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    private List<String> permissions;
}
