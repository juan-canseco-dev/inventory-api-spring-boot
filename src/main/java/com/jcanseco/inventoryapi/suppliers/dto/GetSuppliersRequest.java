package com.jcanseco.inventoryapi.suppliers.dto;

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








