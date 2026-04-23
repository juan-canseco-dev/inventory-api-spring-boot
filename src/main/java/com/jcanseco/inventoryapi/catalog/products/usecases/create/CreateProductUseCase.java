package com.jcanseco.inventoryapi.catalog.products.usecases.create;

import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.catalog.products.dto.CreateProductDto;
import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Long execute(CreateProductDto dto) {
        var supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new DomainException(String.format("The Supplier with the Id {%d} was not found.", dto.getSupplierId())));

        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new DomainException(String.format("The Category with the Id {%d} was not found.", dto.getCategoryId())));

        var unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new DomainException(String.format("The Unit Of Measurement with the Id {%d} was not found.", dto.getUnitId())));

        validatePrices(dto.getPurchasePrice(), dto.getSalePrice());

        var product = Product.builder()
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(dto.getName())
                .purchasePrice(BigDecimal.valueOf(dto.getPurchasePrice()))
                .salePrice(BigDecimal.valueOf(dto.getSalePrice()))
                .build();

        var newProduct = productRepository.save(product);

        var productStock = Stock.builder()
                .product(newProduct)
                .productId(newProduct.getId())
                .quantity(0L)
                .build();

        stockRepository.save(productStock);
        return newProduct.getId();
    }

    private void validatePrices(double purchasePrice, double salePrice) {
        if (salePrice < purchasePrice) {
            throw new DomainException("The Sale Price must be greater than Purchase Price.");
        }
    }
}

