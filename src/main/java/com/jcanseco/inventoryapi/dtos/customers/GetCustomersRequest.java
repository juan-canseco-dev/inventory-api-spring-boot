package com.jcanseco.inventoryapi.dtos.customers;

import com.jcanseco.inventoryapi.validators.orderBy.OrderBy;
import com.jcanseco.inventoryapi.validators.sortorder.SortOrder;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCustomersRequest {

    @Min(1)
    private Integer pageNumber;

    @Min(1)
    private Integer pageSize;

    @OrderBy(
            message = "Invalid Order By field. The following options are valid: 'id', 'dni', 'fullName', 'phone'.",
            fields = {"id", "dni", "fullName", "phone"}
    )
    private String orderBy;

    @SortOrder
    private String sortOrder;

    private String dni;
    private String fullName;
    private String phone;
}
