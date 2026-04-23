package com.jcanseco.inventoryapi.purchases.usecases.getbyid;

import com.jcanseco.inventoryapi.purchases.dto.PurchaseDetailsDto;
import com.jcanseco.inventoryapi.purchases.mapping.PurchaseMapper;
import com.jcanseco.inventoryapi.purchases.persistence.PurchaseRepository;
import com.jcanseco.inventoryapi.shared.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPurchaseByIdUseCase {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    @Transactional(readOnly = true)
    public PurchaseDetailsDto execute(Long purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .map(purchaseMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found", purchaseId)));
    }
}

