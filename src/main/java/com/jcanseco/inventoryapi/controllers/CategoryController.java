package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.dtos.categories.CategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.CreateCategoryDto;
import com.jcanseco.inventoryapi.dtos.categories.GetCategoriesRequest;
import com.jcanseco.inventoryapi.dtos.categories.UpdateCategoryDto;
import com.jcanseco.inventoryapi.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

@Validated
@RestControllerAdvice
@RequestMapping("api/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCategoryDto dto) throws URISyntaxException {
        var categoryId = categoryService.createCategory(dto);
        var location = new URI("/api/categories/" + categoryId);
        return ResponseEntity.created(location).body(categoryId);
    }


    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.Update))")
    @PutMapping("{categoryId}")
    public ResponseEntity<?> update(@PathVariable Long categoryId, @RequestBody @Valid UpdateCategoryDto dto) {
        if (!dto.getCategoryId().equals(categoryId)) {
            return ResponseEntity.badRequest().build();
        }
        categoryService.updateCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.Delete))")
    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> delete(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.View))")
    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCategoriesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            var response = categoryService.getCategories(request);
            return ResponseEntity.ok(response);
        }
        var response = categoryService.getCategoriesPaged(request);
        return ResponseEntity.ok(response);
    }
}
