package com.jcanseco.inventoryapi.dtos.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDto {

    @Size(max = 20)
    @NotEmpty
    @NotBlank
    private String dni;

    @Size(max =  20)
    @NotEmpty
    @NotBlank
    private String phone;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String fullName;

    @NotNull
    @Valid
    private AddressDto address;
}
