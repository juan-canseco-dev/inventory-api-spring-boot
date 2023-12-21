package com.jcanseco.inventoryapi.dtos.customers;

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
}
