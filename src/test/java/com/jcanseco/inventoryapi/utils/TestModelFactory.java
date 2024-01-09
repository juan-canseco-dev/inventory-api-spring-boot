package com.jcanseco.inventoryapi.utils;

import com.jcanseco.inventoryapi.entities.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestModelFactory {

    public static Category newCategory(String name) {
        return Category.builder()
                .name(name)
                .build();
    }
    public static Category newCategory(Long categoryId, String name) {
        return Category.builder()
                .id(categoryId)
                .name(name)
                .build();
    }

    public static UnitOfMeasurement newUnit(Long unitId, String name) {
        return UnitOfMeasurement.builder()
                .id(unitId)
                .name(name)
                .build();
    }

    public static UnitOfMeasurement newUnit(String name) {
        return UnitOfMeasurement.builder()
                .name(name)
                .build();
    }

    public static Address newAddress(String country, String state, String city, String zipCode, String street) {
        return Address.builder()
                .country(country)
                .state(state)
                .city(city)
                .zipCode(zipCode)
                .street(street)
                .build();
    }

    private static Address defaultCustomerAddress() {
        return Address.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }


    public static Customer newCustomer(String dni, String phone, String fullName) {
        return Customer.builder()
                .dni(dni)
                .phone(phone)
                .fullName(fullName)
                .address(defaultCustomerAddress())
                .build();
    }

    public static Customer newCustomer(String dni, String phone, String fullName, Address address) {
        return Customer.builder()
                .dni(dni)
                .phone(phone)
                .fullName(fullName)
                .address(address)
                .build();
    }

    public static Customer newCustomer(Long customerId, String dni, String phone, String fullName, Address address) {
        return Customer.builder()
                .id(customerId)
                .dni(dni)
                .phone(phone)
                .fullName(fullName)
                .address(address)
                .build();
    }

    public static Customer newCustomer(Long customerId, String dni, String phone, String fullName) {
        return Customer.builder()
                .id(customerId)
                .dni(dni)
                .phone(phone)
                .fullName(fullName)
                .address(defaultCustomerAddress())
                .build();
    }


    public static SupplierAddress newSupplierAddress(String country,
                                                     String state,
                                                     String city,
                                                     String zipCode,
                                                     String street) {
        return SupplierAddress.builder()
                .country(country)
                .state(state)
                .city(city)
                .zipCode(zipCode)
                .street(street)
                .build();
    }

    private static SupplierAddress defaultSupplierAddress() {
        return SupplierAddress.builder()
                .country("Mexico")
                .state("Sonora")
                .city("Hermosillo")
                .zipCode("83200")
                .street("Center")
                .build();
    }

    public static Supplier newSupplier(String companyName,
                                    String contactName,
                                    String contactPhone,
                                    SupplierAddress address) {
        return Supplier.builder()
                .companyName(companyName)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .address(address)
                .build();
    }

    public static Supplier newSupplier(String companyName,
                                    String contactName,
                                    String contactPhone) {
        return Supplier.builder()
                .companyName(companyName)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .address(defaultSupplierAddress())
                .build();
    }

    public static Supplier newSupplier(Long supplierId,
                                    String companyName,
                                    String contactName,
                                    String contactPhone,
                                    SupplierAddress address) {
        return Supplier.builder()
                .id(supplierId)
                .companyName(companyName)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .address(address)
                .build();
    }

    public static Supplier newSupplier(Long supplierId,
                                    String companyName,
                                    String contactName,
                                    String contactPhone) {
        return Supplier.builder()
                .id(supplierId)
                .companyName(companyName)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .address(defaultSupplierAddress())
                .build();
    }

    public static Product newProduct(
                                  Supplier supplier,
                                  Category category,
                                  UnitOfMeasurement unit,
                                  String name,
                                  Double purchasePrice,
                                  Double salePrice) {
        return Product.builder()
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(name)
                .quantity(0L)
                .purchasePrice(BigDecimal.valueOf(purchasePrice))
                .salePrice(BigDecimal.valueOf(salePrice))
                .build();
    }

    public static Product newProduct(
            Supplier supplier,
            Category category,
            UnitOfMeasurement unit,
            String name,
            Long quantity,
            Double purchasePrice,
            Double salePrice) {
        return Product.builder()
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(name)
                .quantity(quantity)
                .purchasePrice(BigDecimal.valueOf(purchasePrice))
                .salePrice(BigDecimal.valueOf(salePrice))
                .build();
    }

    public static Product newProduct(
            Long productId,
            Supplier supplier,
            Category category,
            UnitOfMeasurement unit,
            String name,
            Double purchasePrice,
            Double salePrice) {
        return Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(name)
                .quantity(0L)
                .purchasePrice(BigDecimal.valueOf(purchasePrice))
                .salePrice(BigDecimal.valueOf(salePrice))
                .build();
    }

    public static Product newProduct(
            Long productId,
            Supplier supplier,
            Category category,
            UnitOfMeasurement unit,
            String name,
            Long quantity,
            Double purchasePrice,
            Double salePrice) {
        return Product.builder()
                .id(productId)
                .supplier(supplier)
                .category(category)
                .unit(unit)
                .name(name)
                .quantity(quantity)
                .purchasePrice(BigDecimal.valueOf(purchasePrice))
                .salePrice(BigDecimal.valueOf(salePrice))
                .build();
    }

    public static PurchaseItem newPurchaseItem(Long itemId,
                                               Long productId,
                                               String productName,
                                               String productUnit,
                                               Long quantity,
                                               Double purchasePrice)
    {
        return PurchaseItem.builder()
                .id(itemId)
                .productId(productId)
                .productName(productName)
                .productUnit(productUnit)
                .quantity(quantity)
                .price(BigDecimal.valueOf(purchasePrice))
                .total(BigDecimal.valueOf(purchasePrice * quantity))
                .build();
    }


    public static PurchaseItem newPurchaseItem(Long productId,
                                               String productName,
                                               String productUnit,
                                               Long quantity,
                                               Double purchasePrice)
    {
        return PurchaseItem.builder()
                .productId(productId)
                .productName(productName)
                .productUnit(productUnit)
                .quantity(quantity)
                .price(BigDecimal.valueOf(purchasePrice))
                .total(BigDecimal.valueOf(purchasePrice * quantity))
                .build();
    }

    public static PurchaseItem newPurchaseItem(Product product, Long quantity) {
        return PurchaseItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productUnit(product.getUnit().getName())
                .quantity(quantity)
                .price(product.getPurchasePrice())
                .total(product.getPurchasePrice().multiply(BigDecimal.valueOf(quantity)))
                .build();
    }

    public static Purchase newPurchase(Supplier supplier,
                                       List<PurchaseItem> items) {
        return Purchase.builder()
                .supplier(supplier)
                .items(items)
                .total(items.stream()
                        .map(PurchaseItem::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .build();
    }

    public static Purchase newPurchase(Supplier supplier,
                                       List<PurchaseItem> items,
                                       LocalDateTime createdAt) {
        return Purchase.builder()
                .supplier(supplier)
                .items(items)
                .total(items.stream()
                        .map(PurchaseItem::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .createdAt(createdAt)
                .build();
    }

    public static Purchase newPurchase(Long purchaseId,
                                       Supplier supplier,
                                       List<PurchaseItem> items) {
        return Purchase.builder()
                .id(purchaseId)
                .supplier(supplier)
                .items(items)
                .total(items.stream()
                        .map(PurchaseItem::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .build();
    }


    public static Purchase newPurchase(Long purchaseId,
                                       Supplier supplier,
                                       List<PurchaseItem> items,
                                       LocalDateTime createdAt) {
        return Purchase.builder()
                .id(purchaseId)
                .supplier(supplier)
                .items(items)
                .total(items.stream()
                        .map(PurchaseItem::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .createdAt(createdAt)
                .build();
    }

}
