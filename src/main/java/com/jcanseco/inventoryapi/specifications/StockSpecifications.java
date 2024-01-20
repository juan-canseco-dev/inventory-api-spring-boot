package com.jcanseco.inventoryapi.specifications;

import com.jcanseco.inventoryapi.entities.Stock;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public class StockSpecifications {
    public static Specification<Stock> byProductIds(List<Long> productIds) {
        return (root, query, builder) -> root.get("productId").in(productIds);
    }
}
