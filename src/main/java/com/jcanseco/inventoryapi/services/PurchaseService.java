package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.purchases.CreatePurchaseDto;
import com.jcanseco.inventoryapi.dtos.purchases.GetPurchasesRequest;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseDetailsDto;
import com.jcanseco.inventoryapi.dtos.purchases.PurchaseDto;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Purchase;
import com.jcanseco.inventoryapi.entities.PurchaseItem;
import com.jcanseco.inventoryapi.entities.Stock;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.PurchaseRepository;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    private List<Product> getProductsFromHashMap(HashMap<Long, Long> productsWithQuantities) {
        return productsWithQuantities.keySet()
                .stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new DomainException(String.format("The Product with the Id {%d} was not found.", productId)))
                ).toList();
    }

    @Transactional
    public Long createPurchase(CreatePurchaseDto dto) {

        var supplier = supplierRepository.findById(dto.getSupplierId()).orElseThrow(() -> new DomainException(String.format("Supplier with the Id : {%d} was not found.", dto.getSupplierId())));
        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = getProductsFromHashMap(productsWithQuantities);

        var items = products.stream().map(p -> PurchaseItem.builder()
                .product(p)
                .productId(p.getId())
                .productName(p.getName())
                .productUnit(p.getUnit().getName())
                .quantity(productsWithQuantities.get(p.getId()))
                .price(p.getPurchasePrice())
                .total(p.getPurchasePrice().multiply(BigDecimal.valueOf(productsWithQuantities.get(p.getId()))))
                .build()
        ).toList();

        var total = items.stream().map(PurchaseItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        var purchase = Purchase.builder()
                .supplier(supplier)
                .items(items)
                .total(total)
                .build();

        var newPurchase = purchaseRepository.saveAndFlush(purchase);

        var stocks = products.stream().map(Product::getStock).toList();

        for(Stock stock : stocks) {
            var quantity = productsWithQuantities.get(stock.getProductId());
            var newQuantity = stock.getQuantity() + quantity;
            stock.setQuantity(newQuantity);
        }

        stockRepository.saveAllAndFlush(stocks);

        return newPurchase.getId();
    }

    public PurchaseDetailsDto getPurchaseById(Long purchaseId) {
        return null;
    }

    public List<PurchaseDto> getPurchases(GetPurchasesRequest request) {
        return null;
    }

    public PagedList<PurchaseDto> getPurchasesPage(GetPurchasesRequest request) {
        return null;
    }
}
