package com.jcanseco.inventoryapi.dtos;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    private Long categoryId;
    private String name;
}
