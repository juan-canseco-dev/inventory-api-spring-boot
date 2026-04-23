package com.jcanseco.inventoryapi.orders.dto;

import com.jcanseco.inventoryapi.shared.validation.query.orderby.OrderBy;
import com.jcanseco.inventoryapi.shared.validation.query.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.*;

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








