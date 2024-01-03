package com.jcanseco.inventoryapi.dtos.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {
    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String name;
}
