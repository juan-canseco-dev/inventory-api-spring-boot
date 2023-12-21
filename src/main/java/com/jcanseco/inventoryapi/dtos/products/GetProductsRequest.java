package com.jcanseco.inventoryapi.dtos.products;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            message = "Invalid Order By field. The following options are valid: 'id', 'name', 'quantity' 'supplier.companyName', 'category.name', 'unit.name'.",
            fields = {"id", "name", "quantity", "supplier.companyName", "category.name", "unit.name"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    private String name;
    private Long supplierId;
    private Long categoryId;
    private Long unitId;
}
