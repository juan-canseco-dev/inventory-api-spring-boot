package com.jcanseco.inventoryapi.identity.roles.dto;

import com.jcanseco.inventoryapi.identity.permissions.domain.Permissions;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;





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








