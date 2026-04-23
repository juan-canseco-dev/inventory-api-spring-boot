package com.jcanseco.inventoryapi.inventory.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.shared.errors.DomainException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;





@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products_stock")
public class Stock {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updatedAt;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public void addStock(Long addQuantity) {
        var newQuantity = getQuantity() + addQuantity;
        setQuantity(newQuantity);
    }

    public void removeStock(Long removeQuantity) {
        if (removeQuantity > getQuantity()) {
            throw new DomainException(String.format("Invalid stock operation: Cannot remove %d units from inventory. Current stock quantity for ProductId %d: %d.", removeQuantity, getProductId(), getQuantity()));
        }
        var newQuantity = getQuantity() - removeQuantity;
        setQuantity(newQuantity);
    }
}






