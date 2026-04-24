-- =========================================
-- AuthSphereJWT - Test Data Initialization
-- Purpose: Preload users for testing authentication flow
-- =========================================
INSERT INTO users (username, password, role)
VALUES 
('admin', '$2a$10$uIkRu/ts0xIhzlSnXypycOb3/yhbt9JnV1eRFJ4gK6k6tRosehqR.', 'ROLE_ADMIN'); //Password@123

INSERT INTO users (username, password, role)
VALUES 
('user', '$2a$10$uIkRu/ts0xIhzlSnXypycOb3/yhbt9JnV1eRFJ4gK6k6tRosehqR.', 'ROLE_USER'); //Password@123

