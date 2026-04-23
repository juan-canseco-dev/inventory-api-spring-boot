package com.jcanseco.inventoryapi.identity.roles.dto;

import com.jcanseco.inventoryapi.shared.validation.query.orderby.OrderBy;
import com.jcanseco.inventoryapi.shared.validation.query.sortorder.SortOrder;
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








