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
public class UpdateUserDto {

    @NotEmpty
    @NotBlank
    private String userId;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String fullName;
}
