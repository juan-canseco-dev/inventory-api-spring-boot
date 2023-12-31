INSERT INTO units_of_measurement (id, name)
VALUES (1, 'Piece');

INSERT INTO categories (id, name)
VALUES (1, 'Electronics');

INSERT INTO supplier_address (id, country, state, city, zip_code, street)
VALUES
    (1, 'United States', 'California', 'San Francisco', '94105', '123 Main St'),
    (2, 'Australia', 'New South Wales', 'Sydney', '2000', '101 Tech Blvd');

INSERT INTO suppliers (id, company_name, contact_name, contact_phone, address_id)
VALUES (1, 'ABC Corp', 'John Doe', '555-1234-1', 1),
       (2, 'Tech Solutions Inc', 'Alice Brown', '555-1234-4', 2);

INSERT INTO products (id, supplier_id, category_id, unit_id, name, quantity, purchase_price, sale_price)
VALUES (1,1,1,1,'Laptop', 0, 800.00, 1200.00),
       (2,1,1,1, 'Smartphone', 0, 500.00, 800.00),
       (3,1,1,1, 'HD Television', 0, 600.00, 900.00),
       (4,1,1,1,'Tablet', 0, 200.00, 300.00),
       (5,1,1,1, 'Digital Camera', 0, 150.00, 250.00),
       (6,1,1,1, 'Air Purifier', 0, 80.00, 120.00),
       (7,1,1,1,'Refrigerator', 0, 700.00, 1000.00),
       (8,1,1,1, 'Bluetooth Speaker', 0, 30.00, 50.00),
       (9,1,1,1, 'Vacuum Cleaner', 0, 90.00, 150.00),
       (10,1,1,1, 'Toaster', 0, 25.00, 40.00),
       (11,2,1,1,'Coffee Maker', 0, 50.00, 100.00),
       (12,2,1,1, 'Desk Chair', 0, 100.00, 150.00),
       (13,2,1,1, 'Washing Machine', 0, 400.00, 700.00),
       (14,2,1,1,'Office Desk', 0, 120.00, 200.00),
       (15,2,1,1, 'Dinnerware Set', 0, 60.00, 100.00),
       (16,2,1,1, 'Bookshelf', 0, 80.00, 120.00),
       (17,2,1,1,'Microwave Oven', 0, 120.00, 200.00),
       (18,2,1,1, 'Coffee Table', 0, 60.00, 100.00),
       (19,2,1,1, 'Vacuum Cleaner', 0, 90.00, 150.00),
       (20,2,1,1, 'Sleeping Bag', 0, 40.00, 60.00);

-- Supplier 1 Purchases

-- Purchase 1
INSERT INTO purchases (id, supplier_id, total, created_at)
VALUE (1, 1, 13000.00, '2023-05-01');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (1, 1, 'Laptop', 'Piece', 10, 800.00, 8000.00),
       (1, 2, 'Smartphone', 'Piece', 10, 500.00, 5000.00);

-- Purchase 2
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (2, 1, 8000.00, '2023-05-08');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (2, 3, 'HD Television', 'Piece', 10, 600.00, 6000.00),
       (2, 4, 'Tablet', 'Piece', 10, 200.00, 2000.00);

-- Purchase 3
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (3, 1, 2300.00, '2023-05-15');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (3, 5, 'Digital Camera', 'Piece', 10, 150.00, 1500.00),
       (3, 6, 'Air Purifier', 'Piece', 10, 80.00, 800.00);


-- Purchase 4
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (4, 1, 7300.00, '2023-05-22');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (4, 7, 'Refrigerator', 'Piece', 10, 700.00, 7000.00),
       (4, 8, 'Bluetooth Speaker', 'Piece', 10, 30.00, 300.00);

-- Purchase 5
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (5, 1, 1150.00, '2023-05-29');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (5, 9, 'Vacuum Cleaner', 'Piece', 10, 90.00, 900.00),
       (5, 10, 'Toaster', 'Piece', 10, 25.00, 250.00);



-- Supplier Two Purchases

-- Purchase 6
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (6, 2, 1500.00, '2023-06-05');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (6, 11, 'Coffee Maker', 'Piece', 10, 50.00, 500.00),
       (6, 12, 'Desk Chair', 'Piece', 10, 100.00, 1000.00);

-- Purchase 7
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (7, 2, 5200.00, '2023-06-12');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (7, 13, 'Washing Machine', 'Piece', 10, 400.00, 4000.00),
       (7, 14, 'Office Desk', 'Piece', 10, 120.00, 1200.00);

-- Purchase 8
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (8, 2, 1400.00, '2023-06-19');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (8, 15, 'Dinnerware Set', 'Piece', 10, 60.00, 600.00),
       (8, 16, 'Bookshelf', 'Piece', 10, 80.00, 800.00);


-- Purchase 9
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (9, 2, 1800.00, '2023-06-26');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (9, 17, 'Microwave Oven', 'Piece', 10, 120.00, 1200.00),
       (9, 18, 'Coffee Table', 'Piece', 10, 60.00, 600.00);

-- Purchase 10
INSERT INTO purchases (id, supplier_id, total, created_at)
    VALUE (10, 2, 1300.00, '2023-07-03');

INSERT INTO purchase_items (purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES (10, 19, 'Vacuum Cleaner', 'Piece', 10, 90.00, 900.00),
       (10, 20, 'Sleeping Bag', 'Piece', 10, 40.00, 400.00);
