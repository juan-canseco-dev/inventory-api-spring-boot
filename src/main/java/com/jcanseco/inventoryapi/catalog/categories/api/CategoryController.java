package com.jcanseco.inventoryapi.catalog.categories.api;

import com.jcanseco.inventoryapi.catalog.categories.dto.CategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.dto.CreateCategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.dto.GetCategoriesRequest;
import com.jcanseco.inventoryapi.catalog.categories.dto.UpdateCategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.usecases.create.CreateCategoryUseCase;
import com.jcanseco.inventoryapi.catalog.categories.usecases.delete.DeleteCategoryUseCase;
import com.jcanseco.inventoryapi.catalog.categories.usecases.getall.GetCategoriesUseCase;
import com.jcanseco.inventoryapi.catalog.categories.usecases.getbyid.GetCategoryByIdUseCase;
import com.jcanseco.inventoryapi.catalog.categories.usecases.update.UpdateCategoryUseCase;
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
@RequestMapping("api/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.Create))")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCategoryDto dto) throws URISyntaxException {
        var categoryId = createCategoryUseCase.execute(dto);
        var location = new URI("/api/categories/" + categoryId);
        return ResponseEntity.created(location).body(categoryId);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.Update))")
    @PutMapping("{categoryId}")
    public ResponseEntity<?> update(@PathVariable Long categoryId, @RequestBody @Valid UpdateCategoryDto dto) {
        if (!dto.getCategoryId().equals(categoryId)) {
            return ResponseEntity.badRequest().build();
        }
        updateCategoryUseCase.execute(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.Delete))")
    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> delete(@PathVariable Long categoryId) {
        deleteCategoryUseCase.execute(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.View))")
    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long categoryId) {
        var response = getCategoryByIdUseCase.execute(categoryId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Categories, @Action.View))")
    @GetMapping
    public ResponseEntity<?> getAll(@Valid GetCategoriesRequest request) {
        if (request.getPageSize() == null || request.getPageNumber() == null) {
            return ResponseEntity.ok(getCategoriesUseCase.execute(request));
        }
        return ResponseEntity.ok(getCategoriesUseCase.executePaged(request));
    }
}
