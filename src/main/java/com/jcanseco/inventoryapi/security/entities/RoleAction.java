package com.jcanseco.inventoryapi.security.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "role_actions")
public class RoleAction {
    @Id
    private String id;

    @Column(nullable = false, updatable = false, length = 50)
    private String name;

    @Column(nullable = false, updatable = false, name = "required", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean required;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}

