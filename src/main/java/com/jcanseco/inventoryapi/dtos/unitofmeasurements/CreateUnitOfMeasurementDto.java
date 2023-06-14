package com.jcanseco.inventoryapi.dtos.unitofmeasurements;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUnitOfMeasurementDto {
    private String name;
}
