package com.jcanseco.inventoryapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressDto {

    @NotEmpty
    @Size(max = 50)
    private String country;

    @NotEmpty
    @Size(max = 50)
    private String state;

    @NotEmpty
    @Size(max = 50)
    private String city;


    @NotEmpty
    @Size(max = 10)
    private String zipCode;


    @NotEmpty
    @Size(max = 75)
    private String street;
}
