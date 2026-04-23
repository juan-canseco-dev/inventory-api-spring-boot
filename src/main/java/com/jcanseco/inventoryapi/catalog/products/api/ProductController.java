package com.jcanseco.inventoryapi.catalog.products.api;

import com.jcanseco.inventoryapi.catalog.products.dto.CreateProductDto;
import com.jcanseco.inventoryapi.catalog.products.dto.GetProductsRequest;
import com.jcanseco.inventoryapi.catalog.products.dto.ProductDetailsDto;
import com.jcanseco.inventoryapi.catalog.products.dto.ProductDto;
import com.jcanseco.inventoryapi.catalog.products.dto.UpdateProductDto;
import com.jcanseco.inventoryapi.catalog.products.usecases.create.CreateProductUseCase;
import com.jcanseco.inventoryapi.catalog.products.usecases.delete.DeleteProductUseCase;
import com.jcanseco.inventoryapi.catalog.products.usecases.getall.GetProductsUseCase;
import com.jcanseco.inventoryapi.catalog.products.usecases.getbyid.GetProductByIdUseCase;
import com.jcanseco.inventoryapi.catalog.products.usecases.update.UpdateProductUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestControllerAdvice
@RequestMapping("api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final GetProductsUseCase getProductsUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Products, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateProductDto dto) throws URISyntaxException {
        var productId = createProductUseCase.execute(dto);
        var location = new URI("/api/products/" + productId);
        return ResponseEntity.created(location).body(productId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Products, @Action.Update))")
    @PutMapping("{productId}")
    public ResponseEntity<?> update(@PathVariable Long productId, @RequestBody @Valid UpdateProductDto dto) {
        if (!dto.getProductId().equals(productId)) {
            return ResponseEntity.badRequest().build();
        }
        updateProductUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Products, @Action.Delete))")
    @DeleteMapping("{productId}")
    public ResponseEntity<ProductDto> delete(@PathVariable Long productId) {
        deleteProductUseCase.execute(productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Products, @Action.View))")
    @GetMapping("{productId}")
    public ResponseEntity<ProductDetailsDto> getById(@PathVariable Long productId) {
        var response = getProductByIdUseCase.execute(productId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Products, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetProductsRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getProductsUseCase.execute(request));
        }
        return ResponseEntity.ok(getProductsUseCase.executePaged(request));
    }
}
