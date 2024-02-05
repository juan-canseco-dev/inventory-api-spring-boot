DELETE FROM role_permissions;
DELETE FROM roles;
DELETE FROM users;

-- Roles
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

-- Users
INSERT INTO users (id, role_id, full_name, email, created_at, updated_at)
VALUES (1, 1, 'John Doe', 'john_doe@gmail.com', '2023-05-01', '2023-05-02'),
       (2, 1, 'Jane Doe', 'jane_doe@gmail.com',  '2023-05-08', '2023-05-09'),
       (3, 2, 'John Smith', 'john_smith@gmail.com',  '2023-05-15', '2023-05-16'),
       (4, 2, 'Jane Smith', 'jane_smith@gmail.com',  '2023-05-22', '2023-05-23');
