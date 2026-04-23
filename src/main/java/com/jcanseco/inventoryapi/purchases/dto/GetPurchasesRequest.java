package com.jcanseco.inventoryapi.purchases.dto;

import com.jcanseco.inventoryapi.shared.validation.query.orderby.OrderBy;
import com.jcanseco.inventoryapi.shared.validation.query.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.*;







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
            message = "Invalid Order By field. The following options are valid: 'id', 'supplier', 'total', 'orderedAt', 'arrivedAt', 'arrived'.",
            fields = {"id", "supplier", "total", "orderedAt", "arrivedAt", "arrived"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    @Min(1)
    private Long supplierId;

    private Boolean arrived;

    private LocalDateTime orderedAtStartDate;
    private LocalDateTime orderedAtEndDate;

    private LocalDateTime arrivedAtStartDate;
    private LocalDateTime arrivedAtEndDate;
}








