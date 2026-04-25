package com.jcanseco.inventoryapi.dashboard.usecases;

import com.jcanseco.inventoryapi.dashboard.dto.GetTopSoldProductsRequest;
import com.jcanseco.inventoryapi.dashboard.dto.TopSoldProductDto;
import com.jcanseco.inventoryapi.dashboard.persistence.ProductReportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTopSoldProductsUseCase {

    private final ProductReportsRepository repository;

    public List<TopSoldProductDto> execute(GetTopSoldProductsRequest request) {
        return repository.getTopSoldProducts(
                request.getStartDate(),
                request.getEndDate(),
                Pageable.ofSize(request.getLimit())
        );
    }
}
