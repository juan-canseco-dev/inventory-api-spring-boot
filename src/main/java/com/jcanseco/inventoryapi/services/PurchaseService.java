package com.jcanseco.inventoryapi.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.purchases.*;
import com.jcanseco.inventoryapi.entities.Product;
import com.jcanseco.inventoryapi.entities.Purchase;
import com.jcanseco.inventoryapi.entities.PurchaseItem;
import com.jcanseco.inventoryapi.entities.Stock;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.mappers.PurchaseMapper;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.PurchaseRepository;
import com.jcanseco.inventoryapi.repositories.StockRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.specifications.StockSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import static com.jcanseco.inventoryapi.specifications.PurchaseSpecifications.*;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final IndexUtility indexUtility;

    private List<PurchaseItem> getItemsByProducts(List<Product> products, HashMap<Long, Long> quantities) {
        return products.stream().map(p -> PurchaseItem.builder()
                .product(p)
                .productId(p.getId())
                .productName(p.getName())
                .productUnit(p.getUnit().getName())
                .quantity(quantities.get(p.getId()))
                .price(p.getPurchasePrice())
                .total(p.getPurchasePrice().multiply(BigDecimal.valueOf(quantities.get(p.getId()))))
                .build()).toList();
    }

    private BigDecimal getTotalFromItems(List<PurchaseItem> items) {
        return items.stream().map(PurchaseItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Long createPurchase(CreatePurchaseDto dto) {

        var supplier = supplierRepository.findById(dto.getSupplierId()).orElseThrow(() -> new DomainException(String.format("Supplier with the Id : {%d} was not found.", dto.getSupplierId())));
        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());

        var items = getItemsByProducts(products, productsWithQuantities);

        var total = getTotalFromItems(items);

        var purchase = Purchase.builder()
                .supplier(supplier)
                .items(items)
                .total(total)
                .build();

        var newPurchase = purchaseRepository.saveAndFlush(purchase);

        return newPurchase.getId();
    }

    @Transactional
    public void receivePurchase(Long purchaseId) {

        var purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found.", purchaseId)));

        if (purchase.isArrived()) {
            throw new DomainException(String.format("The purchase with ID %d has already been marked as 'arrived'.", purchaseId));
        }

        var productsWithQuantities = purchase.getItems().stream()
                .collect(Collectors.toMap(PurchaseItem::getProductId, PurchaseItem::getQuantity));

        var stocks = stockRepository.findAll(
                StockSpecifications.byProductIds(productsWithQuantities.keySet().stream().toList())
        );

        for (Stock stock : stocks) {
            var currentQuantity = stock.getQuantity();
            var newQuantity = productsWithQuantities.get(stock.getProductId());
            var updatedQuantity = currentQuantity + newQuantity;
            stock.setQuantity(updatedQuantity);
        }

        purchase.setArrived(true);
        purchase.setArrivedAt(LocalDateTime.now());

        purchaseRepository.saveAndFlush(purchase);

        stockRepository.saveAllAndFlush(stocks);
    }

    @Transactional
    public void deletePurchase(Long purchaseId) {
        var purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found.", purchaseId)));

        if (purchase.isArrived()) {
            throw new DomainException(String.format("Cannot delete the purchase with ID %d because it has already arrived.", purchaseId));
        }

        purchaseRepository.delete(purchase);
    }

    @Transactional
    public void updatePurchase(UpdatePurchaseDto dto) {

        var purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found.", dto.getPurchaseId())));

        if (purchase.isArrived()) {
            throw new DomainException(String.format("Purchase with Id %d has already arrived and cannot be updated.", dto.getPurchaseId()));
        }

        var productsWithQuantities = dto.getProductsWithQuantities();
        var products = productRepository.findAllById(productsWithQuantities.keySet());
        var items = getItemsByProducts(products, productsWithQuantities);
        var total = getTotalFromItems(items);

        purchase.setItems(items);
        purchase.setTotal(total);

        purchaseRepository.saveAndFlush(purchase);
    }


    @Transactional(readOnly = true)
    public PurchaseDetailsDto getPurchaseById(Long purchaseId) {
        return purchaseRepository
                .findById(purchaseId)
                .map(purchaseMapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase with the Id : {%d} was not found", purchaseId)));
    }

    private Specification<Purchase> orderBySpecification(Specification<Purchase> spec, GetPurchasesRequest request) {

        var orderBy = !StringUtils.hasText(request.getOrderBy())? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());

        return switch (orderBy) {
            case "id" -> isAscending? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "supplier" -> isAscending? orderBySupplierAsc(spec) : orderBySupplierDesc(spec);
            case "total" -> isAscending? orderByTotalAsc(spec) : orderByTotalDesc(spec);
            case "arrived" -> isAscending? orderByArrivedAsc(spec) : orderByArrivedDesc(spec);
            case "arrivedAt" -> isAscending? orderByArrivedAtAsc(spec) : orderByArrivedAtDesc(spec);
            case "orderedAt" -> isAscending? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
            default -> isAscending? orderByOrderedAtAsc(spec) : orderByOrderedAtDesc(spec);
        };
    }

    private Specification<Purchase> composeSpecification(GetPurchasesRequest request) {

        Specification<Purchase> spec = Specification.where(null);

        if (request.getSupplierId() != null) {
            var supplier = supplierRepository.findById(request.getSupplierId()).orElse(null);
            spec = spec.and(bySupplier(supplier));
        }

        if (request.getArrived() != null) {
            spec = spec.and(byArrived(request.getArrived()));
        }

        if (request.getOrderedAtStartDate() != null && request.getOrderedAtEndDate() != null) {
            spec = spec.and(byOrderedBetween(request.getOrderedAtStartDate(), request.getOrderedAtEndDate()));
        }

        if (request.getArrivedAtStartDate() != null && request.getArrivedAtEndDate() != null) {
            spec = spec.and(byArrivedBetween(request.getArrivedAtStartDate(), request.getArrivedAtEndDate()));
        }

        return orderBySpecification(spec, request);
    }

    public List<PurchaseDto> getPurchases(GetPurchasesRequest request) {
        var spec = composeSpecification(request);
        return purchaseRepository.findAll(spec)
                .stream()
                .map(purchaseMapper::entityToDto)
                .toList();
    }

    public PagedList<PurchaseDto> getPurchasesPage(GetPurchasesRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = purchaseRepository.findAll(specification, pageRequest);

        return purchaseMapper.pageToPagedList(page);
    }
}