package com.jcanseco.inventoryapi.security.dtos.roles;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetRolesRequest {

    @Min(1)
    private Integer pageNumber;

    @Min(1)
    private Integer pageSize;

    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id', 'name', 'createdAt', 'updatedAt'",
            fields = {"id", "name", "createdAt", "updatedAt"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    private String name;
}
