package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.products.*;
import com.jcanseco.inventoryapi.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/products")
@RestController
public class ProductsController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid CreateProductDto dto) {
        return ResponseEntity.ok(service.createProduct(dto));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable Long productId, @RequestBody @Valid UpdateProductDto dto) {
        if (!dto.getProductId().equals(productId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.updateProduct(dto));
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<ProductDto> delete(@PathVariable Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductDetailsDto> getById(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getProductById(productId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetProductsRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getProducts(request);
            return ResponseEntity.ok(response);
        }
        var response = service.getProductsPaged(request);
        return ResponseEntity.ok(response);
    }
}
