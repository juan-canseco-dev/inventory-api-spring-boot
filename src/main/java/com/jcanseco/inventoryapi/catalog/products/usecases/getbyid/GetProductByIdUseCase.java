package com.jcanseco.inventoryapi.catalog.products.usecases.getbyid;

import com.jcanseco.inventoryapi.catalog.products.dto.ProductDetailsDto;
import com.jcanseco.inventoryapi.catalog.products.mapping.ProductMapper;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetProductByIdUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductDetailsDto execute(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", productId)));
    }
}

