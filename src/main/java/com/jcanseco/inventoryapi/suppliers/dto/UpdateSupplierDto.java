package com.jcanseco.inventoryapi.suppliers.dto;

import com.jcanseco.inventoryapi.shared.address.Address;
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








