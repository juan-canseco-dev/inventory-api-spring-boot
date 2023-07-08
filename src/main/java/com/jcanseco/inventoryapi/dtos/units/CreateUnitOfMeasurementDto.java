package com.jcanseco.inventoryapi.dtos.units;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUnitOfMeasurementDto {
    @Size(max = 50)
    @NotEmpty
    private String name;
}
