package com.jcanseco.inventoryapi.dtos.units;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUnitsOfMeasurementRequest {

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
