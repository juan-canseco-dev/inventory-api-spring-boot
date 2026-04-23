package com.jcanseco.inventoryapi.suppliers.dto;

import lombok.*;





@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private Long id;
    private String companyName;
    private String contactName;
    private String contactPhone;
}








