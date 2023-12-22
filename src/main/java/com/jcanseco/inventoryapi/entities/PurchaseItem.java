package com.jcanseco.inventoryapi.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "purchase_items")
public class PurchaseItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false, length = 50)
    private String productUnit;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false, precision = 8, scale = 2)
    BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;
}
