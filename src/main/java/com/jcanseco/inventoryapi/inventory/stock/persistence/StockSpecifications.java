package com.jcanseco.inventoryapi.inventory.stock.persistence;

import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecifications {

    public static Specification<Stock> byProductIds(List<Long> productIds) {
        return (root, query, builder) -> {
            if (productIds == null || productIds.isEmpty()) {
                return builder.conjunction();
            }

            return root.join("product").get("id").in(productIds);
        };
    }
}





