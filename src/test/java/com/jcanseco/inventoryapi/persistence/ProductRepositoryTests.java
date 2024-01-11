package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import com.jcanseco.inventoryapi.repositories.ProductRepository;
import com.jcanseco.inventoryapi.repositories.SupplierRepository;
import com.jcanseco.inventoryapi.repositories.UnitOfMeasurementRepository;
import com.jcanseco.inventoryapi.specifications.ProductSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import static com.jcanseco.inventoryapi.utils.TestModelFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UnitOfMeasurementRepository unitRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Sql("/multiple-products.sql")
    public void createProductShouldGenerateId() {

        var supplier = supplierRepository.findById(1L).orElseThrow();
        var category = categoryRepository.findById(1L).orElseThrow();
        var unit = unitRepository.findById(1L).orElseThrow();

        var product = newProduct(supplier, category, unit, "New Product", 19.99, 20.99);
        var newProduct = productRepository.saveAndFlush(product);

        assertNotNull(newProduct);
        assertTrue(newProduct.getId() > 0);
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByNameLikeShouldReturnList() {
        var name = "l";
        var spec = ProductSpecifications.byNameLike(name);
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsBySupplierShouldReturnList() {
        var supplier = supplierRepository.findById(1L).orElseThrow();
        var spec = ProductSpecifications.bySupplier(supplier);
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByCategoryShouldReturnList() {
        var category = categoryRepository.findById(1L).orElseThrow();
        var spec = ProductSpecifications.byCategory(category);
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsByUnitShouldReturnList() {
        var unit = unitRepository.findById(1L).orElseThrow();
        var spec = ProductSpecifications.byUnit(unit);
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(10, products.size());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByIdAscFirstProductIdShouldBeOne() {
        var spec = ProductSpecifications.orderByIdAsc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(1L, firstProduct.getId());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByIdDescFirstProductIdShouldBeTwenty() {
        var spec = ProductSpecifications.orderByIdDesc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(20L, firstProduct.getId());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByNameAscFirstProductNameShouldStartWithA() {
        var expectedProductName = "Air Purifier";
        var spec = ProductSpecifications.orderByNameAsc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedProductName, firstProduct.getName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByNameDescFirstProductNameShouldStartWithW() {
        var expectedProductName = "Washing Machine";
        var spec = ProductSpecifications.orderByNameDesc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedProductName, firstProduct.getName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByQuantityAscFirstProductQuantityShouldBe10() {
        var expectedProductQuantity = 10L;
        var spec = ProductSpecifications.orderByQuantityAsc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedProductQuantity, firstProduct.getQuantity());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByQuantityDescFirstProductQuantityShouldBe200() {
        var expectedProductQuantity = 200L;
        var spec = ProductSpecifications.orderByQuantityDesc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedProductQuantity, firstProduct.getQuantity());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderBySupplierAscSupplierCompanyNameShouldStartWithA() {
        var expectedCompanyName = "ABC Corp";
        var spec = ProductSpecifications.orderBySupplierAsc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedCompanyName, firstProduct.getSupplier().getCompanyName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderBySupplierDescSupplierCompanyNameShouldStartWithT() {
        var expectedCompanyName = "Tech Solutions Inc";
        var spec = ProductSpecifications.orderBySupplierDesc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedCompanyName, firstProduct.getSupplier().getCompanyName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByCategoryAscCategoryNameShouldStartWithE() {
        var expectedCategoryName = "Electronics";
        var spec = ProductSpecifications.orderByCategoryAsc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedCategoryName, firstProduct.getCategory().getName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByCategoryDescCategoryNameShouldStartWithH() {
        var expectedCategoryName = "Home and Garden";
        var spec = ProductSpecifications.orderByCategoryDesc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertNotNull(products);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedCategoryName, firstProduct.getCategory().getName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByUnitAscUnitNameShouldStartWithP() {
        var expectedUnitName = "Piece";
        var spec = ProductSpecifications.orderByUnitAsc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedUnitName, firstProduct.getUnit().getName());
    }

    @Test
    @Sql("/multiple-products.sql")
    public void getProductsOrderByUnitDescUnitNameShouldStartWithS() {
        var expectedUnitName = "Set";
        var spec = ProductSpecifications.orderByUnitDesc(
                Specification.where(null)
        );
        var products = productRepository.findAll(spec);
        assertEquals(20, products.size());
        var firstProduct = products.get(0);
        assertEquals(expectedUnitName, firstProduct.getUnit().getName());
    }
}
