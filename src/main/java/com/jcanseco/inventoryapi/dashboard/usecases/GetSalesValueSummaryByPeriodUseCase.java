package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetValueSummaryRequest;
import com.jcanseco.inventoryapi.dashboard.dto.InventoryValueSummaryDto;
import com.jcanseco.inventoryapi.dashboard.persistence.OrderReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSalesValueSummaryByPeriodUseCase {
    private final OrderReportsRepository orderRepository;
    public InventoryValueSummaryDto execute(GetValueSummaryRequest request) {
        var totalValue = orderRepository.getOrdersSummaryByPeriod(request.getStartDate(), request.getEndDate());
        return new InventoryValueSummaryDto(totalValue);
    }
}
