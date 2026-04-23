package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetProductsWithLowStockCountRequest;
import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductsWithLowStockCountUseCase {
    private final ProductReportsRepository repository;
    public Long execute(GetProductsWithLowStockCountRequest request) {
        return repository.getProductsWithLowStockCount(request.getStockThreshold());
    }
}
