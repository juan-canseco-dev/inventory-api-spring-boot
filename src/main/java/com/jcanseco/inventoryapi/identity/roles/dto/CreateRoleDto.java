package com.jcanseco.inventoryapi.identity.roles.dto;

import com.jcanseco.inventoryapi.identity.permissions.domain.Permissions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;






@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleDto {

    @Size(max = 50)
    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    @UniqueElements
    @NotEmpty
    private List<String> permissions;
}








