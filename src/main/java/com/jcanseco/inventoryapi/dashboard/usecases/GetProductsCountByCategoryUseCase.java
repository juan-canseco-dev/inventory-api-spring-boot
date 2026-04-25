package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.ProductsByCategoryDto;
import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductsCountByCategoryUseCase {
    private final ProductReportsRepository repository;

    public List<ProductsByCategoryDto> execute() {
        return repository.getProductsCountByCategory();
    }
}
