package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.CategoryDto;
import com.jcanseco.inventoryapi.dtos.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.UpdateCategoryDto;
import com.jcanseco.inventoryapi.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@AllArgsConstructor
@RestControllerAdvice
@RequestMapping("api/categories")
@RestController
public class CategoriesController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CreateCategoryDto dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long categoryId, @RequestBody @Valid UpdateCategoryDto dto) {
        if (!dto.getCategoryId().equals(categoryId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.updateCategory(dto));
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> delete(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(service.getCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCategoriesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = service.getCategories(request.getName());
            return ResponseEntity.ok(response);
        }
        var response = service.getCategoriesPaged(request);
        return ResponseEntity.ok(response);
    }
}
