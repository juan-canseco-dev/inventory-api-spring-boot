package com.jcanseco.inventoryapi.customers.dto;

import com.jcanseco.inventoryapi.shared.address.Address;
import com.jcanseco.inventoryapi.shared.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;



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








