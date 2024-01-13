DELETE FROM customers;
-- Insert data into customers table
INSERT INTO customers (id, dni, phone, full_name,
                       customer_address_country,
                       customer_address_state,
                       customer_address_city,
                       customer_address_zip_code,
                       customer_address_street)
VALUES (1,'123456789', '555-1234-1', 'John Doe', 'United States', 'California', 'San Francisco', '94105', '123 Main St'),
       (2,'987654321', '555-1234-2', 'Jane Smith', 'United Kingdom', 'England', 'London', 'EC1A 1BB', '456 High St'),
       (3,'456789012', '555-1234-3', 'Bob Johnson','Canada', 'Ontario', 'Toronto', 'M5V 2A8', '789 Maple Ave'),
       (4,'789012345', '555-1234-4', 'Alice Brown', 'Australia', 'New South Wales', 'Sydney', '2000', '101 Tech Blvd'),
       (5,'234567890', '555-1234-5', 'David Wilson','Germany', 'Bavaria', 'Munich', '80331', '234 Innovation Strasse'),
       (6,'567890123', '555-1234-6', 'Linda Miller','Brazil', 'Sao Paulo', 'Sao Paulo', '01310-200', '345 Sunny Ave' ),
       (7,'890123456', '555-1234-7', 'Chris Taylor', 'China', 'Shanghai', 'Shanghai', '200000', '456 Eco Street'),
       (8,'345678901', '555-1234-8', 'Emily White', 'South Africa', 'Gauteng', 'Johannesburg', '2000', '567 Summit Road'),
       (9,'678901234', '555-1234-9', 'Michael Lee','India', 'Maharashtra', 'Mumbai', '400001', '678 Urban Lane' ),
       (10,'901234567', '555-1234-10', 'Olivia Garcia','New Zealand', 'Auckland', 'Auckland', '1010', '789 Ocean View');