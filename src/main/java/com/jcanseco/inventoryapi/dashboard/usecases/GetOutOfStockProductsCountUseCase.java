package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOutOfStockProductsCountUseCase {
    private final ProductReportsRepository repository;

    public Long execute() {
        return repository.getOutOfStockProductsCount();
    }
}
