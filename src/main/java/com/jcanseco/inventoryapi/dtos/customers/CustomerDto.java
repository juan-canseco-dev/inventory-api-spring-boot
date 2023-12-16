package com.jcanseco.inventoryapi.dtos.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String dni;
    private String phone;
    private String fullName;
    private AddressDto address;
}
