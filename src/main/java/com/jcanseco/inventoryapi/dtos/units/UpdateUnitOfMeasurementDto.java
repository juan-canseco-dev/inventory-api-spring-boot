package com.jcanseco.inventoryapi.dtos.units;

import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUnitOfMeasurementDto {
    @Min(1)
    @NotNull
    private Long unitOfMeasurementId;

    @Size(max = 50)
    @NotEmpty
    @NotBlank
    private String name;
}
