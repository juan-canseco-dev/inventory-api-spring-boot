package com.jcanseco.inventoryapi.dtos.units;

import lombok.*;


@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasurementDto {
    private Long id;
    private String name;
}
