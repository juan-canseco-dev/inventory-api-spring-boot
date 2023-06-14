package com.jcanseco.inventoryapi.dtos.categories;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    @Min(1)
    private Long categoryId;
    @Size(max = 50)
    @NotEmpty
    private String name;
}
