package com.jcanseco.inventoryapi.dtos.purchases;

import com.jcanseco.inventoryapi.dtos.suppliers.SupplierDto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetailsDto {
    private Long id;
    private SupplierDto supplier;
    private Double total;
    private List<PurchaseItemDto> items;
    private boolean arrived;
    private LocalDateTime orderedAt;
    private LocalDateTime arrivedAt;
}
