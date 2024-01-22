package com.jcanseco.inventoryapi.entities;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "purchases")
public class Purchase {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;


    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseItem> items;

    @Column(nullable = false, name = "arrived", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean arrived;

    @Column(name = "arrived_at")
    private LocalDateTime arrivedAt;

    @CreationTimestamp
    @Column(name = "ordered_at", nullable = false, updatable = false)
    private LocalDateTime orderedAt;


    public void update(List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        if (isArrived()) {
            throw new DomainException(String.format("Purchase with Id %d has already arrived and cannot be updated.", getId()));
        }
        var items = getItemsByProducts(products, productsWithQuantities);
        var total = getTotalFromItems(items);
        setItems(items);
        setTotal(total);
    }

    public void markAsArrived() {
        if (isArrived()) {
            throw new DomainException(String.format("The purchase with ID %d has already been marked as 'arrived'.", getId()));
        }
        setArrived(true);
        setArrivedAt(LocalDateTime.now());
    }

    public static Purchase createNew(Supplier supplier, List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        var items = getItemsByProducts(products, productsWithQuantities);
        var total = getTotalFromItems(items);
        return Purchase.builder()
                .supplier(supplier)
                .items(items)
                .total(total)
                .build();
    }

    private static List<PurchaseItem> getItemsByProducts(List<Product> products, HashMap<Long, Long> quantities) {
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

    private static BigDecimal getTotalFromItems(List<PurchaseItem> items) {
        return items.stream().map(PurchaseItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
