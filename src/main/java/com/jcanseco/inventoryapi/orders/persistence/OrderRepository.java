package com.jcanseco.inventoryapi.orders.persistence;

import com.jcanseco.inventoryapi.orders.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("""
     SELECT DISTINCT o
     FROM Order o 
     LEFT JOIN FETCH o.customer
     LEFT join FETCH o.items
     WHERE o.id = :id
     """)

    Optional<Order> findWithDetailsById(Long id);
}






