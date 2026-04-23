package com.jcanseco.inventoryapi.dashboard.dto;

public record TopSoldProductDto(
        Long productId,
        String productName,
        Long totalSold) {
}
