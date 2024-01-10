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

INSERT INTO products (supplier_id, category_id, unit_id, name, quantity, purchase_price, sale_price)
VALUES (1,1,1,'Laptop', 0, 800.00, 1200.00),
       (1,1,1, 'Smartphone', 0, 500.00, 800.00),
       (1,1,1, 'HD Television', 0, 600.00, 900.00),
       (1,1,1,'Tablet', 0, 200.00, 300.00),
       (1,1,1, 'Digital Camera', 0, 150.00, 250.00),
       (1,1,1, 'Air Purifier', 0, 80.00, 120.00),
       (1,1,1,'Refrigerator', 0, 700.00, 1000.00),
       (1,1,1, 'Bluetooth Speaker', 0, 30.00, 50.00),
       (1,1,1, 'Vacuum Cleaner', 0, 90.00, 150.00),
       (1,1,1, 'Toaster', 0, 25.00, 40.00),
       (2,2,2,'Coffee Maker', 0, 50.00, 100.00),
       (2,2,2, 'Desk Chair', 0, 100.00, 150.00),
       (2,2,2, 'Washing Machine', 0, 400.00, 700.00),
       (2,2,2,'Office Desk', 0, 120.00, 200.00),
       (2,2,2, 'Dinnerware Set', 0, 60.00, 100.00),
       (2,2,2, 'Bookshelf', 0, 80.00, 120.00),
       (2,2,2,'Microwave Oven', 0, 120.00, 200.00),
       (2,2,2, 'Coffee Table', 0, 60.00, 100.00),
       (2,2,2, 'Vacuum Cleaner', 0, 90.00, 150.00),
       (2,2,2, 'Sleeping Bag', 0, 40.00, 60.00);
