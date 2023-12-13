package com.jcanseco.inventoryapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressDto {
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String country;

    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String state;

    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String city;


    @NotEmpty
    @NotNull
    @Size(max = 10)
    private String zipCode;


    @NotEmpty
    @NotNull
    @Size(max = 75)
    private String street;
}
