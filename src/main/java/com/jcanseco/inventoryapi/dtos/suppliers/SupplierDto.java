package com.jcanseco.inventoryapi.dtos.suppliers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private int id;
    private String companyName;
    private String contactName;
    private String contactPhone;
}
