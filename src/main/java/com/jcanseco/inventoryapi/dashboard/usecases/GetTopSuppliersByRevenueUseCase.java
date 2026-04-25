package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetTopSuppliersByRevenueRequest;
import com.jcanseco.inventoryapi.dashboard.dto.TopSupplierByRevenueDto;
import com.jcanseco.inventoryapi.dashboard.persistence.SuppliersReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTopSuppliersByRevenueUseCase {
    private final SuppliersReportsRepository repository;
    public List<TopSupplierByRevenueDto> execute(GetTopSuppliersByRevenueRequest request) {
        return repository.getTopSuppliersByRevenue(
                request.getStartDate(),
                request.getEndDate(),
                Pageable.ofSize(request.getLimit())
        );
    }
}
