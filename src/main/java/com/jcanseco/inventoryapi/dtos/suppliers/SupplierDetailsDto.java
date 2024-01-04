package com.jcanseco.inventoryapi.dtos.suppliers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import lombok.*;

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
