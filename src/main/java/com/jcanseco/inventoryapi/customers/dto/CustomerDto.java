package com.jcanseco.inventoryapi.customers.dto;

import lombok.*;

@EqualsAndHashCode
@Builder
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








