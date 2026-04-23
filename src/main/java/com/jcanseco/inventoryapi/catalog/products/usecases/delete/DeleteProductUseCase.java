package com.jcanseco.inventoryapi.catalog.products.usecases.delete;

import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public void execute(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", productId)));

        productRepository.delete(product);
    }
}

