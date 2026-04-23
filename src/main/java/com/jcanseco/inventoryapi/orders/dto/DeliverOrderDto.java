package com.jcanseco.inventoryapi.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliverOrderDto {

    @Min(1)
    Long orderId;

    @NotEmpty
    @NotBlank
    @Size(max = 300)
    String comment;
}







