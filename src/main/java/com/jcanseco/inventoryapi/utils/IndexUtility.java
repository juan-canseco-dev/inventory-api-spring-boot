package com.jcanseco.inventoryapi.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Objects;

@Component
public class IndexUtility {

    // Convert to 0-based index
    public Integer toZeroBasedIndex(Integer pageNumber) {
        Objects.requireNonNull(pageNumber);
        return pageNumber > 0 ? pageNumber - 1 : pageNumber;
    }

    // Check if the sort order is ascending; default is ascending
    public boolean isAscendingOrder(String sortOrderText) {
        if (!StringUtils.hasText(sortOrderText))
            return true;
        return sortOrderText.equals("asc") || sortOrderText.equals("ascending");
    }
}
