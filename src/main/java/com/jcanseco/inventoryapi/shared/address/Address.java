package com.jcanseco.inventoryapi.shared.address;

import lombok.*;





@EqualsAndHashCode
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    protected String country;
    protected String state;
    protected String city;
    protected String zipCode;
    protected String street;
}






