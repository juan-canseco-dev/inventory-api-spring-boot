package com.jcanseco.inventoryapi.dtos.units;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUnitOfMeasurementDto {
    @Min(1)
    private Long unitOfMeasurementId;
    @Size(max = 50)
    @NotEmpty
    private String name;
}
