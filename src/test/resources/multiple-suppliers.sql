INSERT INTO supplier_address (id, country, state, city, zip_code, street) VALUES
                                                                         (1,'United States', 'California', 'San Francisco', '94105', '123 Main St'),
                                                                         (2,'United Kingdom', 'England', 'London', 'EC1A 1BB', '456 High St'),
                                                                         (3,'Canada', 'Ontario', 'Toronto', 'M5V 2A8', '789 Maple Ave'),
                                                                         (4,'Australia', 'New South Wales', 'Sydney', '2000', '101 Tech Blvd'),
                                                                         (5,'Germany', 'Bavaria', 'Munich', '80331', '234 Innovation Strasse'),
                                                                         (6,'Brazil', 'Sao Paulo', 'Sao Paulo', '01310-200', '345 Sunny Ave'),
                                                                         (7,'China', 'Shanghai', 'Shanghai', '200000', '456 Eco Street'),
                                                                         (8,'South Africa', 'Gauteng', 'Johannesburg', '2000', '567 Summit Road'),
                                                                         (9,'India', 'Maharashtra', 'Mumbai', '400001', '678 Urban Lane'),
                                                                         (10,'New Zealand', 'Auckland', 'Auckland', '1010', '789 Ocean View');

-- Insert data into suppliers table
INSERT INTO suppliers (company_name, contact_name, contact_phone, address_id) VALUES
                                                                               ('ABC Corp', 'John Doe', '555-1234-1', 1),
                                                                               ('XYZ Ltd', 'Jane Smith', '555-1234-2', 2),
                                                                               ('123 Enterprises', 'Bob Johnson', '555-1234-3', 3),
                                                                               ('Tech Solutions Inc', 'Alice Brown', '555-1234-4', 4),
                                                                               ('Global Innovations', 'David Wilson', '555-1234-5', 5),
                                                                               ('Sunshine Foods', 'Linda Miller', '555-1234-6', 6),
                                                                               ('Green Energy Co', 'Chris Taylor', '555-1234-7', 7),
                                                                               ('Peak Performance', 'Emily White', '555-1234-8', 8),
                                                                               ('City Builders', 'Michael Lee', '555-1234-9', 9),
                                                                               ('Oceanic Ventures', 'Olivia Garcia', '555-1234-10', 10);