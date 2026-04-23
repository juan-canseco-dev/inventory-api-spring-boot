package com.jcanseco.inventoryapi.identity.roles.persistence;

import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;





@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByNameIgnoreCase(String name);
}





