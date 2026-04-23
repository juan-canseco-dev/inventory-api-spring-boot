package com.jcanseco.inventoryapi.dashboard.dto;

import java.math.BigDecimal;

public record TopCustomerByRevenueDto(Long id, String fullName, BigDecimal totalRevenue) {
}
