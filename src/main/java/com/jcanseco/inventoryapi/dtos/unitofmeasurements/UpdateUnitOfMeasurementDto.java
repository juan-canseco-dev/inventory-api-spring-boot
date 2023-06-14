package com.jcanseco.inventoryapi.dtos.unitofmeasurements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUnitOfMeasurementDto {
    private Long unitOfMeasurementId;
    private String name;
}
