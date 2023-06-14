package com.jcanseco.inventoryapi.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoriesRequest {
    @Min(1)
    private Long pageNumber;
    @Min(1)
    private Long pageSize;
    private String name;
}
