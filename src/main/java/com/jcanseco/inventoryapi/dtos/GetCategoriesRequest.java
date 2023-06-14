package com.jcanseco.inventoryapi.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoriesRequest {
    @Min(1)
    private Integer pageNumber;
    @Min(1)
    private Integer pageSize;
    private String name;
}