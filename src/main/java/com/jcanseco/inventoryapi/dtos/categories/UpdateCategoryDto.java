package com.jcanseco.inventoryapi.dtos.categories;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    @Min(1)
    @NotNull
    private Long categoryId;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String name;
}
