package com.jcanseco.inventoryapi.security.dtos.users;

import jakarta.validation.constraints.*;
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

    @Min(1)
    @NotNull
    private Long roleId;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    @Email
    private String email;

    @Size(max = 30)
    @NotEmpty
    @NotBlank
    private String password;
}
