package com.jcanseco.inventoryapi.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PagedList<T> {

    private final List<T> items;
    private final int pageIndex;
    private final int totalPages;
    private final long rowsCount;
    public PagedList() {
        this.items = null;
        this.pageIndex = 0;
        this.totalPages = 0;
        this.rowsCount = 0;
    }

    public boolean hasPreviousPage() {
        return getPageIndex() > 1;
    }

    public boolean hasNextPage() {
        return getPageIndex() < getTotalPages();
    }
}
