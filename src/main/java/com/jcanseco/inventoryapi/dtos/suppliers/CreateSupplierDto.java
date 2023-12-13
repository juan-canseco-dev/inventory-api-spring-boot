package com.jcanseco.inventoryapi.dtos.suppliers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSupplierDto {

    @Size(max = 50)
    @NotEmpty
    @NotNull
    private String companyName;

    @Size(max = 50)
    @NotEmpty
    @NotNull
    private String contactName;

    @Size(max =  20)
    @NotEmpty
    @NotNull
    private String contactPhone;

    @NotNull
    private AddressDto address;
}
