package com.jcanseco.inventoryapi.dtos.purchases;


import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetPurchasesRequest {

    @Min(1)
    private Integer pageNumber;

    @Min(1)
    private Integer pageSize;

    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id', 'supplier', 'createdAt', 'arrivedAt'.",
            fields = {"id", "supplier", "createdAt", "arrivedAt"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    @Min(1)
    private Long supplierId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
