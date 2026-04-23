package com.jcanseco.inventoryapi.dashboard.dto;


import java.math.BigDecimal;

public record TopSupplierByRevenueDto(Long id, String name, BigDecimal totalRevenue) {

}
