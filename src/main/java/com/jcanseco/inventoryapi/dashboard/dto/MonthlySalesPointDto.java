package com.jcanseco.inventoryapi.dashboard.dto;

import java.math.BigDecimal;

public record MonthlySalesPointDto(
        int year,
        int month,
        BigDecimal totalValue
) {
}
