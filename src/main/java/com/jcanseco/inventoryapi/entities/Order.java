package com.jcanseco.inventoryapi.entities;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @CreationTimestamp
    @Column(name = "ordered_at", nullable = false, updatable = false)
    private LocalDateTime orderedAt;

    @Column(nullable = false, name = "delivered", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean delivered;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    public void update(List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        if (isDelivered()) {
            throw new DomainException(String.format("Order with Id %d has already delivered and cannot be updated.", getId()));
        }
        var items = getItemsByProducts(products, productsWithQuantities);
        var total = getTotalFromItems(items);
        this.items.clear();
        this.items.addAll(items);
        setTotal(total);
    }

    public void deliver() {
        if (isDelivered()) {
            throw new DomainException(String.format("The order with ID %d has already been marked as 'delivered'.", getId()));
        }
        setDelivered(true);
        setDeliveredAt(LocalDateTime.now());
    }

    public static Order createNew(Customer customer, List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        var items = getItemsByProducts(products, productsWithQuantities);
        var total = getTotalFromItems(items);
        return Order.builder()
                .customer(customer)
                .items(items)
                .total(total)
                .build();
    }

    private static List<OrderItem> getItemsByProducts(List<Product> products, HashMap<Long, Long> quantities) {
        return products.stream().map(p -> OrderItem.builder()
                .product(p)
                .productId(p.getId())
                .productName(p.getName())
                .productUnit(p.getUnit().getName())
                .quantity(quantities.get(p.getId()))
                .price(p.getSalePrice())
                .total(p.getSalePrice().multiply(BigDecimal.valueOf(quantities.get(p.getId()))))
                .build())
                .collect(Collectors.toList());
    }

    private static BigDecimal getTotalFromItems(List<OrderItem> items) {
        return items.stream().map(OrderItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
