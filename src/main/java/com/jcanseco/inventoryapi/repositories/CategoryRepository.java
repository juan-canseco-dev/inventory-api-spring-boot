package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CategoryRepository extends JpaRepository<Category, Long>
{
    List<Category> findAllByNameContainingOrderByName(String name);
    Page<Category> findAllByNameContaining(String name, Pageable pageable);
}
