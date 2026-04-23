package com.jcanseco.inventoryapi.customers.dto;

import com.jcanseco.inventoryapi.shared.address.AddressDto;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.*;

@EqualsAndHashCode
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








