package com.jcanseco.inventoryapi.dashboard.dto;

public record ProductWithLowStockDto(
        Long id,
        String name,
        Long stockQuantity
) {

}
