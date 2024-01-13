DELETE FROM products_stock;
DELETE FROM products;
DELETE FROM units_of_measurement;
DELETE FROM categories;
DELETE FROM suppliers;

INSERT INTO units_of_measurement (id, name)
VALUES (1, 'Piece'), (2, 'Set');

INSERT INTO categories (id, name)
VALUES (1, 'Electronics'), (2, 'Home and Garden');

INSERT INTO suppliers (id, company_name, contact_name, contact_phone,
                       supplier_address_country,
                       supplier_address_state,
                       supplier_address_city,
                       supplier_address_zip_code,
                       supplier_address_street)
VALUES (1,'ABC Corp', 'John Doe', '555-1234-1', 'United States', 'California', 'San Francisco', '94105', '123 Main St'),
       (2,'Tech Solutions Inc', 'Alice Brown', '555-1234-4', 'Australia', 'New South Wales', 'Sydney', '2000', '101 Tech Blvd');


INSERT INTO products (id, supplier_id, category_id, unit_id, name, purchase_price, sale_price)
VALUES (1,1,1,1,'Laptop', 800.00, 1200.00),
       (2,1,1,1, 'Smartphone', 500.00, 800.00),
       (3,1,1,1, 'HD Television', 600.00, 900.00),
       (4,1,1,1,'Tablet', 200.00, 300.00),
       (5,1,1,1, 'Digital Camera', 150.00, 250.00),
       (6,1,1,1, 'Air Purifier', 80.00, 120.00),
       (7,1,1,1,'Refrigerator', 700.00, 1000.00),
       (8,1,1,1, 'Bluetooth Speaker', 30.00, 50.00),
       (9,1,1,1, 'Vacuum Cleaner', 90.00, 150.00),
       (10,1,1,1, 'Toaster', 25.00, 40.00),
       (11,2,2,2,'Coffee Maker', 50.00, 100.00),
       (12,2,2,2, 'Desk Chair', 100.00, 150.00),
       (13,2,2,2, 'Washing Machine', 400.00, 700.00),
       (14,2,2,2,'Office Desk', 120.00, 200.00),
       (15,2,2,2, 'Dinnerware Set', 60.00, 100.00),
       (16,2,2,2, 'Bookshelf', 80.00, 120.00),
       (17,2,2,2,'Microwave Oven', 120.00, 200.00),
       (18,2,2,2, 'Coffee Table', 60.00, 100.00),
       (19,2,2,2, 'Vacuum Cleaner', 90.00, 150.00),
       (20,2,2,2, 'Sleeping Bag', 40.00, 60.00);


INSERT INTO products_stock(product_id, quantity)
VALUES (1, 10),
       (2, 20),
       (3, 30),
       (4, 40),
       (5, 50),
       (6, 60),
       (7, 70),
       (8, 80),
       (9, 90),
       (10, 100),
       (11, 110),
       (12, 120),
       (13, 130),
       (14, 140),
       (15, 150),
       (16, 160),
       (17, 170),
       (18, 180),
       (19, 190),
       (20, 200);
