DELETE FROM users;
DELETE FROM roles;

-- Role 1
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (1, 'Dashboard', '2023-05-01', '2023-05-02');

INSERT INTO role_permissions(role_id, permission)
VALUES (1, 'Permissions.Dashboard.View');

-- Role 2
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (2, 'Categories', '2023-05-08', '2023-05-09');

INSERT INTO role_permissions(role_id, permission)
VALUES (2, 'Permissions.Categories.View'),
       (2, 'Permissions.Categories.Create'),
       (2, 'Permissions.Categories.Update'),
       (2, 'Permissions.Categories.Delete');

-- Role 3
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (3, 'Units of Measurement', '2023-05-15', '2023-05-16');

INSERT INTO role_permissions(role_id, permission)
VALUES (3, 'Permissions.UnitsOfMeasurement.View'),
       (3, 'Permissions.UnitsOfMeasurement.Create'),
       (3, 'Permissions.UnitsOfMeasurement.Update'),
       (3, 'Permissions.UnitsOfMeasurement.Delete');

-- Role 4
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (4, 'Suppliers', '2023-05-22', '2023-05-23');

INSERT INTO role_permissions(role_id, permission)
VALUES (4, 'Permissions.Suppliers.View'),
       (4, 'Permissions.Suppliers.Create'),
       (4, 'Permissions.Suppliers.Update'),
       (4, 'Permissions.Suppliers.Delete');


-- Role 5
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (5, 'Products', '2023-05-29', '2023-05-30');

INSERT INTO role_permissions(role_id, permission)
VALUES (5, 'Permissions.Products.View'),
       (5, 'Permissions.Products.Create'),
       (5, 'Permissions.Products.Update'),
       (5, 'Permissions.Products.Delete');

-- Role 6
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (6, 'Purchases', '2023-06-05', '2023-06-06');

INSERT INTO role_permissions(role_id, permission)
VALUES (6, 'Permissions.Purchases.View'),
       (6, 'Permissions.Purchases.Create'),
       (6, 'Permissions.Purchases.Update'),
       (6, 'Permissions.Purchases.Delete');

-- Role 7
INSERT INTO roles (id, name, created_at, updated_at)
VALUES (7, 'Orders', '2023-06-12', '2023-06-13');

INSERT INTO role_permissions(role_id, permission)
VALUES (7, 'Permissions.Orders.View'),
       (7, 'Permissions.Orders.Create'),
       (7, 'Permissions.Orders.Update'),
       (7, 'Permissions.Orders.Delete');
