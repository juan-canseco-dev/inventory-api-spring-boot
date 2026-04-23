package com.jcanseco.inventoryapi.suppliers.dto;

import com.jcanseco.inventoryapi.shared.address.Address;
import com.jcanseco.inventoryapi.shared.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDetailsDto {
    private Long id;
    private String companyName;
    private String contactName;
    private String contactPhone;
    private AddressDto address;
}








