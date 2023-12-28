package com.jcanseco.inventoryapi.persistence.purchases;

import com.jcanseco.inventoryapi.entities.*;
import com.jcanseco.inventoryapi.repositories.*;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.TypeContributor;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;

@DisplayName("Get Purchases By Specifications Persistence Tests")
@SpringBootTest
public class GetPurchasesBySpecificationsTests {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;
    private Supplier supplier1;
    private Supplier supplier2;

    @Transactional
    @BeforeEach
    public void setup() {

        var category = categoryRepository.saveAndFlush(
                newCategory("Electronics")
        );
        var unit = unitRepository.saveAndFlush(
                newUnit("Piece")
        );
        supplier1 = this.supplierRepository.saveAndFlush(
                newSupplier(
                        "ABC Corp",
                        "John Doe",
                        "555-1234-1",
                        newSupplierAddress(
                                "United States",
                                "California",
                                "San Francisco",
                                "94105",
                                "123 Main St"
                        )
                )
        );
        supplier2 = this.supplierRepository.saveAndFlush(
                newSupplier(
                        "Tech Solutions Inc",
                        "Alice Brown",
                        "555-1234-4",
                        newSupplierAddress(
                                "Australia",
                                "New South Wales",
                                "Sydney",
                                "2000",
                                "101 Tech Blvd"
                        )
                )
        );

        var prod1 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit, "Laptop",  800.00, 1200.00)
        );

        var prod2 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Smartphone",  500.00, 800.00)
        );

        var prod3 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"HD Television",  600.00, 900.00)
        );

        var prod4 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Tablet" ,200.00, 300.00)
        );

        var prod5 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Digital Camera",  150.00, 250.00)
        );

        var prod6 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Air Purifier",  80.00, 120.00)
        );

        var prod7 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Refrigerator",  700.00, 1000.00)
        );

        var prod8 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Bluetooth Speaker",  30.00, 50.00)
        );

        var prod9 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Vacuum Cleaner", 90.00, 150.00)
        );

        var prod10 = productRepository.saveAndFlush(
                newProduct(supplier1, category, unit,"Toaster",  25.00, 40.00)
        );

        var prod11 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Coffee Maker",  50.00, 100.00)
        );

        var prod12 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Desk Chair",  100.00, 150.00)
        );

        var prod13 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Washing Machine",  400.00, 700.00)
        );


        var prod14 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Office Desk",  120.00, 200.00)
        );

        var prod15 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Dinnerware Set",  60.00, 100.00)
        );


        var prod16 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Bookshelf",  80.00, 120.00)
        );

        var prod17 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Microwave Oven",  120.00, 200.00)
        );


        var prod18 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Coffee Table",  60.00, 100.00)
        );

        var prod19 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Vacuum Cleaner",  90.00, 150.00)
        );


        var prod20 = productRepository.saveAndFlush(
                newProduct(supplier2, category, unit,"Sleeping Bag",  40.00, 60.00)
        );

        var purchases = List.of(
                newPurchase(
                        supplier1,
                        List.of(
                                newPurchaseItem(prod1, 10L),
                                newPurchaseItem(prod2, 10L)
                        ),
                        LocalDateTime.of(2023, Month.MAY, 1, 0, 0)
                ),
                newPurchase(
                        supplier1,
                        List.of(
                                newPurchaseItem(prod3, 10L),
                                newPurchaseItem(prod4, 10L)
                        ),
                        LocalDateTime.of(2023, Month.MAY, 8, 0, 0)
                ),
                newPurchase(
                        supplier1,
                        List.of(
                                newPurchaseItem(prod5, 10L),
                                newPurchaseItem(prod6, 10L)
                        ),
                        LocalDateTime.of(2023, Month.MAY, 15, 0, 0)
                ),
                newPurchase(
                        supplier1,
                        List.of(
                                newPurchaseItem(prod7, 10L),
                                newPurchaseItem(prod8, 10L)
                        ),
                        LocalDateTime.of(2023, Month.MAY, 22, 0, 0)
                ),
                newPurchase(
                        supplier1,
                        List.of(
                                newPurchaseItem(prod9, 10L),
                                newPurchaseItem(prod10, 10L)
                        ),
                        LocalDateTime.of(2023, Month.MAY, 29, 0, 0)
                ),

                // Another five purchases
                newPurchase(
                        supplier2,
                        List.of(
                                newPurchaseItem(prod11, 5L),
                                newPurchaseItem(prod12, 5L)
                        ),
                        LocalDateTime.of(2023, Month.JUNE, 5, 0, 0)
                ),
                newPurchase(
                        supplier2,
                        List.of(
                                newPurchaseItem(prod13, 5L),
                                newPurchaseItem(prod14, 5L)
                        ),
                        LocalDateTime.of(2023, Month.JUNE, 12, 0, 0)
                ),
                newPurchase(
                        supplier2,
                        List.of(
                                newPurchaseItem(prod15, 5L),
                                newPurchaseItem(prod16, 5L)
                        ),
                        LocalDateTime.of(2023, Month.JUNE, 19, 0, 0)
                ),
                newPurchase(
                        supplier2,
                        List.of(
                                newPurchaseItem(prod17, 5L),
                                newPurchaseItem(prod18, 5L)
                        ),
                        LocalDateTime.of(2023, Month.JUNE, 26, 0, 0)
                ),
                newPurchase(
                        supplier2,
                        List.of(
                                newPurchaseItem(prod19, 5L),
                                newPurchaseItem(prod20, 5L)
                        ),
                        LocalDateTime.of(2023, Month.JULY, 3, 0, 0)
                )
        );
        purchaseRepository.saveAllAndFlush(purchases);
    }

    @AfterEach
    public void cleanup() {
        purchaseRepository.deleteAll();
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void getPurchasesByDateRangeSpecificationShouldReturnList() {
        var startDate = LocalDateTime.of(2023, Month.MAY, 1, 0,9);
        var endDate = LocalDateTime.of(2023, Month.MAY, 28, 0, 0);
        var specification = PurchaseRepository.Specs.byDateRange(startDate, endDate);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(4, purchases.size());
    }

    @Test
    public void getPurchasesBySupplierSpecificationShouldReturnList() {
        var specification = PurchaseRepository.Specs.bySupplier(supplier2);
        var purchases = purchaseRepository.findAll(specification);
        assertNotNull(purchases);
        assertEquals(5, purchases.size());
    }
}
