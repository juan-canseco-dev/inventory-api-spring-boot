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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "country",
                    column = @Column(name = "supplier_address_country", nullable = false, length = 50)
            ),
            @AttributeOverride(
                    name = "state",
                    column = @Column(name = "supplier_address_state", nullable = false, length = 50)
            ),
            @AttributeOverride(
                    name = "city",
                    column = @Column(name = "supplier_address_city", nullable = false, length = 50)
            ),
            @AttributeOverride(
                    name = "zipCode",
                    column = @Column(name = "supplier_address_zip_code", nullable = false, length = 10)
            ),
            @AttributeOverride(
                    name = "street",
                    column = @Column(name = "supplier_address_street", nullable = false, length = 75)
            )
    })
    private Address address;
}
