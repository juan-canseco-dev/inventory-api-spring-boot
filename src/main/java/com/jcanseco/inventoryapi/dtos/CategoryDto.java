package com.jcanseco.inventoryapi.dtos;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
}
