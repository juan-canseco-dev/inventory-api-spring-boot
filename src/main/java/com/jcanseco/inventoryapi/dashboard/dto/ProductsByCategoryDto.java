package com.jcanseco.inventoryapi.dashboard.dto;

public record ProductsByCategoryDto(
        Long categoryId,
        String categoryName,
        Long productCount
) {
}
