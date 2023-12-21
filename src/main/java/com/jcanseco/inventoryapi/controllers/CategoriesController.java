package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/categories")
@RestController
public class CategoriesController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCategoryDto dto) throws URISyntaxException {
        var categoryId = service.createCategory(dto);
        var location = new URI("/api/categories/" + categoryId);
        return ResponseEntity.created(location).body(categoryId);
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<?> update(@PathVariable Long categoryId, @RequestBody @Valid UpdateCategoryDto dto) {
        if (!dto.getCategoryId().equals(categoryId)) {
            return ResponseEntity.badRequest().build();
        }
        service.updateCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> delete(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(service.getCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCategoriesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getCategories(request);
            return ResponseEntity.ok(response);
        }
        var response = service.getCategoriesPaged(request);
        return ResponseEntity.ok(response);
    }
}
