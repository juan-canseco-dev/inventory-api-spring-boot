package com.jcanseco.inventoryapi.catalog.categories.usecases.getall;

import com.jcanseco.inventoryapi.catalog.categories.dto.CategoryDto;
import com.jcanseco.inventoryapi.catalog.categories.dto.GetCategoriesRequest;
import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.catalog.categories.mapping.CategoryMapper;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategorySpecifications;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GetCategoriesUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final IndexUtility indexUtility;

    public List<CategoryDto> execute(GetCategoriesRequest request) {
        var specification = composeSpecification(request);
        return categoryRepository.findAll(specification)
                .stream()
                .map(categoryMapper::entityToDto)
                .toList();
    }

    public PagedList<CategoryDto> executePaged(GetCategoriesRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = categoryRepository.findAll(specification, pageRequest);
        return categoryMapper.pageToPagedList(page);
    }

    private Specification<Category> composeSpecification(GetCategoriesRequest request) {
        Specification<Category> specification = Specification.where(null);

        if (StringUtils.hasText(request.getName())) {
            specification = specification.and(CategorySpecifications.byNameLike(request.getName()));
        }

        var orderByField = !StringUtils.hasText(request.getOrderBy()) ? "id" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return CategorySpecifications.orderBy(specification, orderByField, isAscending);
    }
}

