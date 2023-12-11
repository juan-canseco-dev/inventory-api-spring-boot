package com.jcanseco.inventoryapi.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

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
