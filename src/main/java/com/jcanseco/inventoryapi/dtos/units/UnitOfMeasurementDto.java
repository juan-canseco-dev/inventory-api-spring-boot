package com.jcanseco.inventoryapi.dtos.units;

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
