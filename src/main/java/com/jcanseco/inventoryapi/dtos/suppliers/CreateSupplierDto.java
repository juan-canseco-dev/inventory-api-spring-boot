package com.jcanseco.inventoryapi.dtos.suppliers;

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
public class CreateSupplierDto {

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String companyName;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String contactName;

    @Size(max =  20)
    @NotEmpty
    @NotBlank
    private String contactPhone;

    @NotNull
    @Valid
    private AddressDto address;
}
