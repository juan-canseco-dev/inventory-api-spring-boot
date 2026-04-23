package com.jcanseco.inventoryapi.catalog.products.usecases.update;

import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.catalog.products.dto.UpdateProductDto;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;

    @Transactional
    public void execute(UpdateProductDto dto) {
        var product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException(String.format("The Product with the Id {%d} was not found.", dto.getProductId())));

        if (!product.getSupplier().getId().equals(dto.getSupplierId())) {
            var supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new DomainException(String.format("The Supplier with the Id {%d} was not found.", dto.getSupplierId())));
            product.setSupplier(supplier);
        }

        if (!product.getCategory().getId().equals(dto.getCategoryId())) {
            var category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new DomainException(String.format("The Category with the Id {%d} was not found.", dto.getCategoryId())));
            product.setCategory(category);
        }

        if (!product.getUnit().getId().equals(dto.getUnitId())) {
            var unit = unitRepository.findById(dto.getUnitId())
                    .orElseThrow(() -> new DomainException(String.format("The Unit Of Measurement with the Id {%d} was not found.", dto.getUnitId())));
            product.setUnit(unit);
        }

        validatePrices(dto.getPurchasePrice(), dto.getSalePrice());

        product.setName(dto.getName());
        product.setPurchasePrice(BigDecimal.valueOf(dto.getPurchasePrice()));
        product.setSalePrice(BigDecimal.valueOf(dto.getSalePrice()));

        productRepository.saveAndFlush(product);
    }

    private void validatePrices(double purchasePrice, double salePrice) {
        if (salePrice < purchasePrice) {
            throw new DomainException("The Sale Price must be greater than Purchase Price.");
        }
    }
}

