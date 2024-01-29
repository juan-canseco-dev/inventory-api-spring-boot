package com.jcanseco.inventoryapi.security.dtos.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String fullName;

    @NotEmpty
    @NotBlank
    private String roleId;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String email;

    @Size(max = 30)
    @NotEmpty
    @NotBlank
    private String password;
}
