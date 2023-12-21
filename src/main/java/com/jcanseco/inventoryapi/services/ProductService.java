package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.products.*;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(CreateProductDto dto);
    ProductDto updateProduct(UpdateProductDto dto);
    void deleteProduct(Long productId);
    ProductDetailsDto getProductById(Long productId);
    List<ProductDto> getProducts(GetProductsRequest request);
    List<ProductDto> getProductsPaged(GetProductsRequest request);
}
