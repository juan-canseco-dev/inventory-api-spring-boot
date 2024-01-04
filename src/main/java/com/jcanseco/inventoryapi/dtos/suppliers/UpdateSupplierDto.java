package com.jcanseco.inventoryapi.dtos.suppliers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSupplierDto {
    @Min(1)
    @NotNull
    private Long supplierId;

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
