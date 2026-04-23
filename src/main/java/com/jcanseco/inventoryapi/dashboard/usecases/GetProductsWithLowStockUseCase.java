package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetProductsWithLowStockRequest;
import com.jcanseco.inventoryapi.dashboard.dto.ProductWithLowStockDto;
import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductsWithLowStockUseCase {
    private final ProductReportsRepository repository;
    public List<ProductWithLowStockDto> execute(GetProductsWithLowStockRequest request) {
        return repository.getProductsWithLowStock(
                        request.getStockThreshold(),
                        Pageable.ofSize(request.getLimit())
                );
    }
}
