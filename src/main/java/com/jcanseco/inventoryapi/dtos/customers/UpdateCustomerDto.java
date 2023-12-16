package com.jcanseco.inventoryapi.dtos.customers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDto {
    @Min(1)
    private Long customerId;

    @Size(max = 20)
    @NotEmpty
    @NotNull
    private String dni;

    @Size(max =  20)
    @NotEmpty
    @NotNull
    private String phone;

    @Size(max = 50)
    @NotEmpty
    @NotNull
    private String fullName;

    @NotNull
    private AddressDto address;
}
