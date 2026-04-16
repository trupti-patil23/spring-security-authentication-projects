-- ===============================
-- Initial Roles
-- ===============================
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

-- ===============================
-- Initial Users
-- ===============================
INSERT INTO user (username, password) VALUES 
('user', '$2a$10$gy/0VsvZpYDxmk4.fSjGReXibadroxnkFtX2KHgMXUv9qq9y9r0oi'),  -- password123
('admin', '$2a$10$gy/0VsvZpYDxmk4.fSjGReXibadroxnkFtX2KHgMXUv9qq9y9r0oi'); -- password123

-- ===============================
-- Assign Roles to Users
-- ===============================
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1),  -- user -> ROLE_USER
(2, 2);  -- admin -> ROLE_ADMIN