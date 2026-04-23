package com.jcanseco.inventoryapi.bootstrap.data.catalog;

import com.jcanseco.inventoryapi.catalog.categories.persistence.CategoryRepository;
import com.jcanseco.inventoryapi.catalog.products.domain.Product;
import com.jcanseco.inventoryapi.catalog.products.persistence.ProductRepository;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.inventory.stock.domain.Stock;
import com.jcanseco.inventoryapi.inventory.stock.persistence.StockRepository;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;





@Profile("!test")
@Order(7)
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataInitializer implements ApplicationRunner {

    private static final int NUMBER_OF_PRODUCTS_PER_SUPPLIER = 5;

    private final SupplierRepository supplierRepository;
    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting product data initialization.");

        if (productRepository.count() > 0) {
            log.info("Skipping product initialization because products already exist.");
            return;
        }

        var units = unitOfMeasurementRepository.findAll();
        var categories = categoryRepository.findAll();
        var suppliers = supplierRepository.findAll();

        if (units.isEmpty() || categories.isEmpty() || suppliers.isEmpty()) {
            log.warn("Skipping product initialization because units, categories, or suppliers are missing.");
            return;
        }

        List<Product> products = new ArrayList<>();
        Faker faker = new Faker();
        Random random = new Random();

        int createdProducts = 0;
        for (var supplier : suppliers) {
            for (int i = 0; i < NUMBER_OF_PRODUCTS_PER_SUPPLIER; i++) {
                int randomCategoryIndex = random.nextInt(categories.size());
                int randomUnitIndex = random.nextInt(units.size());

                String productName = faker.commerce().productName();
                BigDecimal purchasePrice = new BigDecimal(faker.commerce().price()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal salePrice = addTwentyPercent(purchasePrice);

                var product = Product.builder()
                        .supplier(supplier)
                        .category(categories.get(randomCategoryIndex))
                        .unit(units.get(randomUnitIndex))
                        .name(productName)
                        .purchasePrice(purchasePrice)
                        .salePrice(salePrice)
                        .build();

                var newProduct = productRepository.save(product);

                var newStock = Stock.builder()
                        .product(newProduct)
                        .productId(newProduct.getId())
                        .quantity(0L)
                        .build();

                stockRepository.save(newStock);

                createdProducts++;
            }
        }

        productRepository.saveAllAndFlush(products);
        log.info("Product data initialization completed. Inserted {} products.", createdProducts);
    }

    private BigDecimal addTwentyPercent(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }

        return value.multiply(new BigDecimal("1.20"), new MathContext(8, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }
}





