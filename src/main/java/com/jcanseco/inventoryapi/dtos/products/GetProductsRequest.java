package com.jcanseco.inventoryapi.dtos.products;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsRequest {

    @Min(1)
    private Integer pageNumber;

    @Min(1)
    private Integer pageSize;

    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id', 'name', 'stock' 'supplier', 'category', 'unit'.",
            fields = {"id", "name", "stock", "supplier", "category", "unit"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    private String name;

    @Min(1)
    private Long supplierId;

    @Min(1)
    private Long categoryId;

    @Min(1)
    private Long unitId;
}
