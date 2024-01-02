package com.jcanseco.inventoryapi.dtos.suppliers;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSuppliersRequest {
    @Min(1)
    private Integer pageNumber;
    @Min(1)
    private Integer pageSize;
    private String companyName;
    private String contactName;
    private String contactPhone;
    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id', 'companyName', 'contactName', 'contactPhone'.",
            fields = {"id", "companyName", "contactName", "contactPhone"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;
}
