package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetValueSummaryRequest;
import com.jcanseco.inventoryapi.dashboard.dto.MonthlySalesPointDto;
import com.jcanseco.inventoryapi.dashboard.persistence.OrderReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMonthlySalesSeriesUseCase {
    private final OrderReportsRepository repository;

    public List<MonthlySalesPointDto> execute(GetValueSummaryRequest request) {
        var monthlySales = repository.getMonthlySalesSummary(request.getStartDate(), request.getEndDate());
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return monthlySales;
        }

        var salesByMonth = new HashMap<YearMonth, BigDecimal>();
        monthlySales.forEach(item -> salesByMonth.put(
                YearMonth.of(item.year(), item.month()),
                item.totalValue()
        ));

        var result = new ArrayList<MonthlySalesPointDto>();
        var currentMonth = YearMonth.from(request.getStartDate());
        var lastMonth = YearMonth.from(request.getEndDate().minusNanos(1));

        while (!currentMonth.isAfter(lastMonth)) {
            result.add(new MonthlySalesPointDto(
                    currentMonth.getYear(),
                    currentMonth.getMonthValue(),
                    salesByMonth.getOrDefault(currentMonth, BigDecimal.ZERO)
            ));
            currentMonth = currentMonth.plusMonths(1);
        }

        return result;
    }
}
