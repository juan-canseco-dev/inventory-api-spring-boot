package com.jcanseco.inventoryapi.services.impl;

import com.jcanseco.inventoryapi.dtos.products.*;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Supplier;
import com.jcanseco.inventoryapi.mappers.ProductMapper;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;
    private final ProductMapper mapper;

    @Transactional
    @Override
    public ProductDto createProduct(CreateProductDto dto) {

        var supplier = supplierRepository.findById(dto.getSupplierId()).orElse(Supplier.builder().build());
        var category = categoryRepository.findById(dto.getCategoryId()).get();
        var unit = unitRepository.findById(dto.getUnitId()).get();

        var product = Product.builder()
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(dto.getName())
                .purchasePrice(BigDecimal.valueOf(dto.getPurchasePrice()))
                .salePrice(BigDecimal.valueOf(dto.getSalePrice()))
                .build();

        var newProduct = productRepository.saveAndFlush(product);
        return mapper.entityToDto(newProduct);
    }

    @Override
    public ProductDto updateProduct(UpdateProductDto dto) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public ProductDetailsDto getProductById(Long productId) {
        return null;
    }

    @Override
    public List<ProductDto> getProducts(GetProductsRequest request) {
        return null;
    }

    @Override
    public List<ProductDto> getProductsPaged(GetProductsRequest request) {
        return null;
    }
}
