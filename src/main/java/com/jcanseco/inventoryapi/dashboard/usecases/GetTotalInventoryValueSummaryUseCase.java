package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.InventoryValueSummaryDto;
import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTotalInventoryValueSummaryUseCase {
    private final ProductReportsRepository repository;
    public InventoryValueSummaryDto execute() {
        var totalValue = repository.getTotalInventoryValue();
        return new InventoryValueSummaryDto(totalValue);
    }
}
