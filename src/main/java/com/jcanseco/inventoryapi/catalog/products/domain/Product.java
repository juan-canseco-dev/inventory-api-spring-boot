package com.jcanseco.inventoryapi.catalog.products.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.catalog.units.domain.UnitOfMeasurement;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.purchases.domain.PurchaseItem;
import com.jcanseco.inventoryapi.orders.domain.OrderItem;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;





@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal salePrice;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private UnitOfMeasurement unit;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PurchaseItem> purchaseItems;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderItems;
}






