package com.jcanseco.inventoryapi.purchases.dto;

import com.jcanseco.inventoryapi.suppliers.dto.SupplierDto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;





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
    private String receiveComments;
    private LocalDateTime orderedAt;
    private LocalDateTime arrivedAt;
}








