package com.jcanseco.inventoryapi.security.dtos.users;

import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {


    @Min(1)
    @NotNull
    private Long userId;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String fullName;
}
