package com.jcanseco.inventoryapi.dtos.orders;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersRequest {

    @Min(1)
    private Integer pageNumber;

    @Min(1)
    private Integer pageSize;

    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id', 'customer', 'total', 'orderedAt', 'deliveredAt', 'delivered'.",
            fields = {"id", "customer", "total", "orderedAt", "deliveredAt", "delivered"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    @Min(1)
    private Long customerId;

    private Boolean delivered;

    private LocalDateTime orderedAtStartDate;
    private LocalDateTime orderedAtEndDate;

    private LocalDateTime deliveredAtStartDate;
    private LocalDateTime deliveredAtEndDate;
}
