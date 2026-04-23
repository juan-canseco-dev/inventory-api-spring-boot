package com.jcanseco.inventoryapi.catalog.categories.dto;

import com.jcanseco.inventoryapi.shared.validation.query.orderby.OrderBy;
import com.jcanseco.inventoryapi.shared.validation.query.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.*;




@EqualsAndHashCode

@Builder
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
    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id' or 'name'.",
            fields = {"id", "name"}
    )
    private String orderBy;
    @SortOrder
    private String sortOrder;
}







