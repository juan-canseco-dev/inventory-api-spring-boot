package com.jcanseco.inventoryapi.security.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleAction> actions;
}
