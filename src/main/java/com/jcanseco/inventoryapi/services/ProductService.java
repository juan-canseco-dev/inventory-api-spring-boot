package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.products.*;

import java.util.List;

public interface ProductService {
    Long createProduct(CreateProductDto dto);
    void updateProduct(UpdateProductDto dto);
    void deleteProduct(Long productId);
    ProductDetailsDto getProductById(Long productId);
    List<ProductDto> getProducts(GetProductsRequest request);
    PagedList<ProductDto> getProductsPaged(GetProductsRequest request);
}
