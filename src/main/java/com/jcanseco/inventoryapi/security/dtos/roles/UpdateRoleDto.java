package com.jcanseco.inventoryapi.security.dtos.roles;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import java.util.List;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {
    @Min(1)
    @NotNull
    private Long roleId;

    @Size(max = 50)
    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    @UniqueElements
    @NotEmpty
    private List<String> permissions;
}
