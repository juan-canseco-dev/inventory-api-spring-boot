package com.jcanseco.inventoryapi.dtos.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import lombok.*;

@EqualsAndHashCode
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsDto {
    private Long id;
    private String dni;
    private String phone;
    private String fullName;
    private AddressDto address;
}
