DELETE FROM purchase_items;
DELETE FROM purchases;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM products_stock;
DELETE FROM products;
DELETE FROM customers;
DELETE FROM units_of_measurement;
DELETE FROM categories;
DELETE FROM suppliers;

INSERT INTO units_of_measurement (id, name)
VALUES (1, 'Piece');

INSERT INTO categories (id, name)
VALUES (1, 'Electronics');

INSERT INTO suppliers (
    id,
    company_name,
    contact_name,
    contact_phone,
    supplier_address_country,
    supplier_address_state,
    supplier_address_city,
    supplier_address_zip_code,
    supplier_address_street
)
VALUES
    (1, 'ABC Corp', 'John Doe', '555-1234-1', 'United States', 'California', 'San Francisco', '94105', '123 Main St'),
    (2, 'Tech Solutions Inc', 'Alice Brown', '555-1234-4', 'Australia', 'New South Wales', 'Sydney', '2000', '101 Tech Blvd');

INSERT INTO customers (
    id,
    dni,
    phone,
    full_name,
    customer_address_country,
    customer_address_state,
    customer_address_city,
    customer_address_zip_code,
    customer_address_street
)
VALUES
    (1, '123456789', '555-0001', 'John Doe', 'United States', 'California', 'San Francisco', '94105', '123 Main St'),
    (2, '987654321', '555-0002', 'Jane Smith', 'United States', 'Colorado', 'Denver', '80202', '456 Market St');

INSERT INTO products (id, supplier_id, category_id, unit_id, name, purchase_price, sale_price)
VALUES
    (1, 1, 1, 1, 'Laptop', 100.00, 150.00),
    (2, 1, 1, 1, 'Mouse', 50.00, 70.00),
    (3, 2, 1, 1, 'Desk', 80.00, 120.00),
    (4, 2, 1, 1, 'Chair', 60.00, 90.00);

INSERT INTO products_stock (product_id, quantity)
VALUES
    (1, 5),
    (2, 2),
    (3, 8),
    (4, 1);

INSERT INTO orders (id, customer_id, total, ordered_at, delivered, delivered_at, deliver_comments)
VALUES
    (1, 1, 510.00, '2024-01-10 00:00:00', true, '2024-01-11 00:00:00', null),
    (2, 2, 600.00, '2024-01-19 00:00:00', true, '2024-01-20 00:00:00', null),
    (3, 1, 480.00, '2024-02-05 00:00:00', false, null, null);

INSERT INTO order_items (id, order_id, product_id, product_name, product_unit, quantity, price, total)
VALUES
    (1, 1, 1, 'Laptop', 'Piece', 2, 150.00, 300.00),
    (2, 1, 2, 'Mouse', 'Piece', 3, 70.00, 210.00),
    (3, 2, 3, 'Desk', 'Piece', 5, 120.00, 600.00),
    (4, 3, 1, 'Laptop', 'Piece', 2, 150.00, 300.00),
    (5, 3, 4, 'Chair', 'Piece', 2, 90.00, 180.00);

INSERT INTO purchases (id, supplier_id, total, ordered_at, arrived, arrived_at, receive_comments)
VALUES
    (1, 1, 300.00, '2024-01-04 00:00:00', true, '2024-01-05 00:00:00', 'received'),
    (2, 2, 280.00, '2024-01-17 00:00:00', true, '2024-01-18 00:00:00', 'received'),
    (3, 1, 100.00, '2024-02-10 00:00:00', false, null, null);

INSERT INTO purchase_items (id, purchase_id, product_id, product_name, product_unit, quantity, price, total)
VALUES
    (1, 1, 1, 'Laptop', 'Piece', 2, 100.00, 200.00),
    (2, 1, 2, 'Mouse', 'Piece', 2, 50.00, 100.00),
    (3, 2, 3, 'Desk', 'Piece', 2, 80.00, 160.00),
    (4, 2, 4, 'Chair', 'Piece', 2, 60.00, 120.00),
    (5, 3, 1, 'Laptop', 'Piece', 1, 100.00, 100.00);
