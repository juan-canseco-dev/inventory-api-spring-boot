package com.jcanseco.inventoryapi.dtos.unitofmeasurements;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUnitsOfMeasurementRequest {
    @Min(1)
    private Integer pageNumber;
    @Min(1)
    private Integer pageSize;
    private String name;
}
