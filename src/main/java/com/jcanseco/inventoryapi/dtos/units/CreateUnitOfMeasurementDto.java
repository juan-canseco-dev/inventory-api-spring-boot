package com.jcanseco.inventoryapi.dtos.units;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUnitOfMeasurementDto {
    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String name;
}
