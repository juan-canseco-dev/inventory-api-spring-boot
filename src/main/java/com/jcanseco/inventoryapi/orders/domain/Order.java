package com.jcanseco.inventoryapi.orders.domain;

import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "ordered_at", nullable = false, updatable = false)
    private LocalDateTime orderedAt;

    @Column(nullable = false, name = "delivered", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean delivered;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(nullable = true, name = "deliver_comments")
    @Size(max = 300)
    private String deliverComments;

    public void update(List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        if (isDelivered()) {
            throw new DomainException(String.format("Order with Id %d has already delivered and cannot be updated.", getId()));
        }
        this.items.clear();
        var newItems = getItemsByProducts(this, products, productsWithQuantities);
        var total = getTotalFromItems(newItems);
        this.items.addAll(newItems);
        setTotal(total);
    }

    public void deliver(String comments) {
        if (isDelivered()) {
            throw new DomainException(String.format("The order with ID %d has already been marked as 'delivered'.", getId()));
        }
        setDelivered(true);
        setDeliveredAt(LocalDateTime.now());
        setDeliverComments(comments);
    }

    public static Order createNew(Customer customer, List<Product> products, HashMap<Long, Long> productsWithQuantities) {
        var newOrder = Order.builder()
                .customer(customer)
                .build();
        var items = getItemsByProducts(newOrder, products, productsWithQuantities);
        var total = getTotalFromItems(items);
        newOrder.setItems(items);
        newOrder.setTotal(total);
        return newOrder;
    }

    private static List<OrderItem> getItemsByProducts(Order order, List<Product> products, HashMap<Long, Long> quantities) {
        return products.stream().map(p -> OrderItem.builder()
                .product(p)
                .productId(p.getId())
                .productName(p.getName())
                .productUnit(p.getUnit().getName())
                .quantity(quantities.get(p.getId()))
                .price(p.getSalePrice())
                .total(p.getSalePrice().multiply(BigDecimal.valueOf(quantities.get(p.getId()))))
                        .order(order)
                .build())
                .collect(Collectors.toList());
    }

    private static BigDecimal getTotalFromItems(List<OrderItem> items) {
        return items.stream().map(OrderItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}






