package com.jcanseco.inventoryapi.purchases.dto;

import jakarta.validation.constraints.Min;
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
public class ReceivePurchaseDto {
    @Min(1)
    private Long purchaseId;

    @NotEmpty
    @NotBlank
    @Size(max = 300)
    private String comment;
}
