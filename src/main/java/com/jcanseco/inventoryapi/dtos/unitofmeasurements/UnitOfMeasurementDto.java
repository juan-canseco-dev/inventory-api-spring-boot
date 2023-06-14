package com.jcanseco.inventoryapi.dtos.unitofmeasurements;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasurementDto {
    private Long id;
    private String name;
}
