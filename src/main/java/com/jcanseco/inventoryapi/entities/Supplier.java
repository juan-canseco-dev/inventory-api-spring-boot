package com.jcanseco.inventoryapi.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "suppliers")
public class Supplier {

    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, length = 50)
    private String companyName;
    @Column(nullable = false, length = 50)
    private String contactName;
    @Column(nullable = false, length = 20)
    private String contactPhone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private SupplierAddress address;
}
