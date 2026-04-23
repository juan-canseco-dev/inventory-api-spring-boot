package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetValueSummaryRequest;
import com.jcanseco.inventoryapi.dashboard.dto.InventoryValueSummaryDto;
import com.jcanseco.inventoryapi.dashboard.persistence.PurchaseReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetInventoryValueSummaryByPeriodUseCase {
    private final PurchaseReportsRepository repository;
    public InventoryValueSummaryDto execute(GetValueSummaryRequest request) {
        var totalValue = repository.getPurchasesSummaryByPeriod(request.getStartDate(), request.getEndDate());
        return  new InventoryValueSummaryDto(totalValue);
    }
}
