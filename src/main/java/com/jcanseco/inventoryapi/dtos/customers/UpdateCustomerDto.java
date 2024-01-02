package com.jcanseco.inventoryapi.dtos.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDto {

    @Min(1)
    @NotNull
    private Long customerId;

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
