package com.jcanseco.inventoryapi.purchases.dto;

import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;





@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long id;
    private String supplier;
    private Double total;
    private boolean arrived;
    private LocalDateTime orderedAt;
    private LocalDateTime arrivedAt;
}







