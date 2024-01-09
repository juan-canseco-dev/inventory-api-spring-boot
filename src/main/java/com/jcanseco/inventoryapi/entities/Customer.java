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
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(name = "uniqueDni", columnNames = "dni")
})
public class Customer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "dni", length = 20)
    private String dni;
    @Column(name = "phone", length = 20)
    private String phone;
    @Column(name = "full_name", length = 50)
    private String fullName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "country",
                    column = @Column(name = "customer_address_country", nullable = false, length = 50)
            ),
            @AttributeOverride(
                    name = "state",
                    column = @Column(name = "customer_address_state", nullable = false, length = 50)
            ),
            @AttributeOverride(
                    name = "city",
                    column = @Column(name = "customer_address_city", nullable = false, length = 50)
            ),
            @AttributeOverride(
                    name = "zipCode",
                    column = @Column(name = "customer_address_zip_code", nullable = false, length = 10)
            ),
            @AttributeOverride(
                    name = "street",
                    column = @Column(name = "customer_address_street", nullable = false, length = 75)
            )
    })
    private Address address;
}
