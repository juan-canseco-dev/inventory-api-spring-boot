package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetTopCustomersByRevenueRequest;
import com.jcanseco.inventoryapi.dashboard.dto.TopCustomerByRevenueDto;
import com.jcanseco.inventoryapi.dashboard.persistence.CustomerReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTopCustomersByRevenueUseCase {
    private final CustomerReportsRepository repository;
    public List<TopCustomerByRevenueDto> execute(GetTopCustomersByRevenueRequest request) {
        return repository.getTopCustomerByRevenue(Pageable.ofSize(request.getLimit()));
    }
}
