-- Insert data into customer_address table
INSERT INTO customer_address (id, country, state, city, zip_code, street) VALUES
                                                                         (1, 'United States', 'California', 'San Francisco', '94105', '123 Main St'),
                                                                         (2,'United Kingdom', 'England', 'London', 'EC1A 1BB', '456 High St'),
                                                                         (3,'Canada', 'Ontario', 'Toronto', 'M5V 2A8', '789 Maple Ave'),
                                                                         (4,'Australia', 'New South Wales', 'Sydney', '2000', '101 Tech Blvd'),
                                                                         (5,'Germany', 'Bavaria', 'Munich', '80331', '234 Innovation Strasse'),
                                                                         (6,'Brazil', 'Sao Paulo', 'Sao Paulo', '01310-200', '345 Sunny Ave'),
                                                                         (7,'China', 'Shanghai', 'Shanghai', '200000', '456 Eco Street'),
                                                                         (8,'South Africa', 'Gauteng', 'Johannesburg', '2000', '567 Summit Road'),
                                                                         (9,'India', 'Maharashtra', 'Mumbai', '400001', '678 Urban Lane'),
                                                                         (10,'New Zealand', 'Auckland', 'Auckland', '1010', '789 Ocean View');

-- Insert data into customers table
INSERT INTO customers (dni, phone, full_name, address_id) VALUES
                                                              ('123456789', '555-1234-1', 'John Doe', 1),
                                                              ('987654321', '555-1234-2', 'Jane Smith', 2),
                                                              ('456789012', '555-1234-3', 'Bob Johnson', 3),
                                                              ('789012345', '555-1234-4', 'Alice Brown', 4),
                                                              ('234567890', '555-1234-5', 'David Wilson', 5),
                                                              ('567890123', '555-1234-6', 'Linda Miller', 6),
                                                              ('890123456', '555-1234-7', 'Chris Taylor', 7),
                                                              ('345678901', '555-1234-8', 'Emily White', 8),
                                                              ('678901234', '555-1234-9', 'Michael Lee', 9),
                                                              ('901234567', '555-1234-10', 'Olivia Garcia', 10);