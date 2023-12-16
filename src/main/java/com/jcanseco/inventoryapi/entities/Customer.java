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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private CustomerAddress address;
}
