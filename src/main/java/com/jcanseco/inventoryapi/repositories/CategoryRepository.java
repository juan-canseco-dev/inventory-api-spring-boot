package com.jcanseco.inventoryapi.repositories;

import com.jcanseco.inventoryapi.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>
{
    List<Category> findAllByNameContaining(String name, Sort sort);
    Page<Category> findAllByNameContaining(String name, Pageable pageable);
}
