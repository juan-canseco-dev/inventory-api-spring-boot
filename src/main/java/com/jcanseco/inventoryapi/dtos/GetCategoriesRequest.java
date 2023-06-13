package com.jcanseco.inventoryapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoriesRequest {
    private int pageNumber;
    private int pageSize;
    private String name;
}
