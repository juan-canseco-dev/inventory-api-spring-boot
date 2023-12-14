package com.jcanseco.inventoryapi.dtos.suppliers;

import com.jcanseco.inventoryapi.dtos.AddressDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSupplierDto {

    @Min(1)
    private long supplierId;

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
