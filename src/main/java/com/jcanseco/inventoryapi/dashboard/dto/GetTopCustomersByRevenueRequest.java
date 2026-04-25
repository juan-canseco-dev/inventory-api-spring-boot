package com.jcanseco.inventoryapi.dashboard.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetTopCustomersByRevenueRequest {
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Min(1)
    private int limit;
}
