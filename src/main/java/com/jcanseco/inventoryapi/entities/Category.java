package com.jcanseco.inventoryapi.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name =  "categories")
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
}
