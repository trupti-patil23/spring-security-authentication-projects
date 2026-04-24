# 🔐 AuthSphere JWT Authentication

A secure Spring Boot + Spring Security + JWT based authentication system implementing:

* Stateless authentication using JWT
* Role-based authorization (USER / ADMIN)
* Secure login & registration flow
* Centralized exception handling
* Ready-to-use Postman collection for testing

---

# 🚀 Features
* ✅ User Registration & Login
* ✅ JWT Token Generation & Validation
* ✅ Role-Based Access Control (RBAC)
* ✅ BCrypt Password Encryption
* ✅ Custom 401 & 403 Exception Handling
* ✅ Global Exception Handling
* ✅ Postman Collection Included
* ✅ Complete Test Cases with Results

---

# 🧠 Tech Stack
* Java 17+
* Spring Boot
* Spring Security
* JWT (jjwt)
* Spring Data JPA
* MySQL
* Maven


---

# 📁 Project Structure

```id="ps1"
authsphere-jwt/
├── src/main/java/com/authspherejwt/
│
│ ├── AuthSphereJwtApplication.java
│
│ ├── controller/
│ │ ├── AuthController.java
│ │ └── TestController.java
│
│ ├── service/
│ │ ├── AuthService.java
│ │ └── CustomUserDetailsService.java
│
│ ├── security/
│ │ ├── JwtAuthFilter.java
│ │ └── JwtService.java
│
│ ├── config/
│ │ └── SecurityConfig.java
│
│ ├── exception/
│ │ ├── GlobalExceptionHandler.java
│ │ ├── JwtAuthenticationEntryPoint.java
│ │ ├── JwtAccessDeniedHandler.java
│ │ ├── InvalidCredentialsException.java
│ │ ├── InvalidTokenException.java
│ │ └── UserAlreadyExistsException.java
│
│ ├── dto/
│ │ ├── LoginRequestDTO.java
│ │ ├── AuthResponseDTO.java
│ │ └── RegisterRequestDTO.java
│
│ ├── entity/
│ │ ├── User.java
│ │ └── Role.java
│
│ ├── repository/
│ │ ├── UserRepository.java
│ │ └── RoleRepository.java
│
├── src/main/resources/
│ ├── application.properties
│ └── data.sql
│
├── postman/
│ │ └── AuthSphere-JWT-Collection.json
│
├── TEST_CASES_AND_RESULTS.md
├── pom.xml
└── README.md
```
---

# 🗄️ Database Schema

### User

* id
* username
* password
* role

---

# 🧪 Initial Data (`data.sql`)

```sql id="sql2"
INSERT INTO users (username, password, role)
VALUES 
('admin', '$2a$10$uIkRu/ts0xIhzlSnXypycOb3/yhbt9JnV1eRFJ4gK6k6tRosehqR.', 'ROLE_ADMIN'); //Password@123

INSERT INTO users (username, password, role)
VALUES 
('user', '$2a$10$uIkRu/ts0xIhzlSnXypycOb3/yhbt9JnV1eRFJ4gK6k6tRosehqR.', 'ROLE_USER'); //Password@123

```

---

# 🔐 Authentication Flow

```id="flow1"
Client → Login API
       ↓
AuthenticationManager
       ↓
UserDetailsService
       ↓
JWT Token Generated
       ↓
Client sends token in Authorization header
       ↓
JwtAuthFilter validates token
       ↓
Spring Security authorizes based on roles
```

---

## 📬 Postman Collection Setup

This project includes a ready-to-use Postman collection for testing all APIs.

📁 File Location:

postman/AuthSphere-JWT-Collection.json


---

## 🔹 How to Import into Postman

Follow these steps in :contentReference[oaicite:0]{index=0}:

1. Open Postman  
2. Click **Import** (top-left corner)  
3. Select **File**  
4. Choose:

postman/AuthSphere-JWT-Collection.json

5. Click **Import**  
6. Collection will appear in workspace  

---

## 🔹 How to Run APIs

### Step 1: Start Backend
Make sure Spring Boot application is running:


http://localhost:8080


---

### Step 2: Run Login APIs First

Run either:

- User Login API
- Admin Login API

After successful login:

- `user_token` is automatically generated  
- `admin_token` is automatically generated  

---

### Step 3: Run Other APIs

Now you can test:

- User APIs
- Admin APIs
- JWT Protected APIs
- Role-Based APIs

---

## 📌 ## 📌 API Reference (Single Source of Truth)

For complete API details,  refer to TEST_CASES_AND_RESULTS.md

This document provides a **complete API testing reference** for the AuthSphere JWT project.

It includes:

- ✔ All API endpoints (Authentication, JWT, Role-Based Access)
- ✔ Request formats (JSON payloads)
- ✔ Expected responses
- ✔ HTTP status codes
- ✔ Success & failure scenarios
- ✔ Real JWT authentication flow
- ✔ Role-based authorization test cases

👉 This file acts as the **single source of truth for API behavior testing**.

---

## 📥 How to Clone the Repository

Follow these steps to set up the project locally:

### 1. Clone Repository
git clone https://github.com/trupti-patil23/authsphere-jwt.git

### 2. Move into Project
cd authsphere-jwt

### 3. Build Project
mvn clean install

### 4. Run Application
mvn spring-boot:run

OR run:
AuthsphereJwtApplication.java

### 5. Open Application
http://localhost:8080

### 6. Import Postman Collection
postman/AuthSphere-JWT-Collection.json

---

# 👩‍💻 Author

**Trupti Patil**
Full Stack Java Developer

* GitHub: https://github.com/trupti-patil23
* LinkedIn: https://linkedin.com/in/patiltruptib

---

