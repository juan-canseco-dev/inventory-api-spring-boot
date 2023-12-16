package com.jcanseco.inventoryapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_address")
@Entity
public class CustomerAddress {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Long id;
    @Column(nullable = false, length = 50)
    protected String country;
    @Column(nullable = false, length = 50)
    protected String state;
    @Column(nullable = false, length = 50)
    protected String city;
    @Column(nullable = false, length = 10)
    protected String zipCode;
    @Column(nullable = false, length = 75)
    protected String street;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private Customer customer;
}
