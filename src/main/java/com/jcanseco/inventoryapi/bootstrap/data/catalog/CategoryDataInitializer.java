package com.jcanseco.inventoryapi.bootstrap.data.catalog;

import com.jcanseco.inventoryapi.catalog.categories.domain.Category;
import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("!test")
@Order(4)
@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryDataInitializer implements ApplicationRunner {

    private final CategoryRepository repository;

    private static final List<String> DEFAULT_CATEGORIES = List.of(
            "Electronics",
            "Groceries",
            "Beverages",
            "Dairy",
            "Bakery",
            "Meat",
            "Vegetables",
            "Fruits",
            "Cleaning Supplies",
            "Personal Care",
            "Office Supplies",
            "Hardware",
            "Clothing",
            "Footwear",
            "Home Appliances",
            "Furniture",
            "Pharmacy",
            "Toys",
            "Pet Supplies",
            "Sports"
    );

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting category data initialization.");

        long existingCategories = repository.count();
        if (existingCategories > 0) {
            log.info("Skipping category seeding. Found {} existing categories.", existingCategories);
            return;
        }

        List<Category> newCategories = DEFAULT_CATEGORIES.stream()
                .map(name -> Category.builder()
                        .name(name)
                        .build())
                .toList();

        log.info("Saving {} categories: {}", newCategories.size(), DEFAULT_CATEGORIES);

        repository.saveAll(newCategories);

        log.info("Category seeding completed successfully. Inserted {} categories.", newCategories.size());
    }
}





