package com.jcanseco.inventoryapi.shared.pagination;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;





@RequiredArgsConstructor
@Getter
public class PagedList<T> {

    private final List<T> items;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;

    @JsonSerialize
    public boolean hasPreviousPage() {
        return pageNumber > 1;
    }
    @JsonSerialize
    public boolean hasNextPage() {
        return pageNumber < totalPages;
    }
}






