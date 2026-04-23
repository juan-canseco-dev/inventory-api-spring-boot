package com.jcanseco.inventoryapi.dashboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetValueSummaryRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
