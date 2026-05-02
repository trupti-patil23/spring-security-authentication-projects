# 🔐 AuthSphere JWT Authentication

A secure Spring Boot + Spring Security + JWT based authentication system implementing:

* Stateless authentication using JWT
* Refresh Token based session management
* Role-based authorization (USER / ADMIN)
* Secure login, registration & logout flow
* Centralized exception handling
* Ready-to-use Postman collection for testing

---

# 🚀 Features

* ✅ User Registration & Login
* ✅ JWT Token Generation & Validation
* ✅ Refresh Token Support (Session Continuity)
* ✅ Logout with Token Invalidation
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

```id="authsphere-jwt"
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
│ │ ├── RefreshTokenService.java        
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
│ │ ├── InvalidRefreshTokenException.java 
│ │ └── UserAlreadyExistsException.java
│
│ ├── dto/
│ │ ├── LoginRequestDTO.java
│ │ ├── AuthResponseDTO.java
│ │ └── RegisterRequestDTO.java
│
│ ├── entity/
│ │ ├── User.java
│ │ └── RefreshToken.java                  
│
│ ├── repository/
│ │ ├── UserRepository.java
│ │ ├── RefreshTokenRepository.java        
│ │ └── (RoleRepository.java removed)
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

### RefreshToken
* id
* token
* user_id
* expiry_date

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

Client → Login API  
↓  
AuthenticationManager  
↓  
UserDetailsService  
↓  
JWT + Refresh Token Generated  
↓  
Client stores both tokens  
↓  
Access Token → API calls  
↓  
Refresh Token → Used when access token expires  
↓  
JwtAuthFilter validates access token  
↓  
Spring Security authorizes based on roles  

---

# 🔄 Refresh Token Flow

* Access token is short-lived
* Refresh token is long-lived and stored in DB
* When access token expires:
  → Client calls `/auth/refresh`
  → System validates refresh token from DB
  → New access token is generated
* If refresh token is invalid or deleted:
  → 401 Unauthorized

---

# 🚪 Logout Flow

* User calls `/auth/logout`
* Refresh token is deleted from database
* Access token cannot be revoked directly (stateless JWT)
* After logout:
  → Refresh token becomes invalid
  → User must login again

---

## 🔐 JWT Token Expiry Configuration

In this project, both Access Token and Refresh Token expiration times are externally configured in the `application.properties` file for flexibility and environment-based configuration.

---

### ⚙️ Configuration Details

```properties
# JWT access token expiration (short-lived for security for 5 min)
jwt.access-token-expiration=300000   

# JWT refresh token expiration (long-lived for session management for 7 days)
jwt.refresh-token-expiration=604800000  
```

---

 ** Access Token (5 minutes)**  
  Used for API authentication. It is short-lived to reduce security risk if compromised.

 ** Refresh Token (7 days)**  
  Stored in database and used to generate new access tokens without requiring the user to log in again.

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

---

### 🚀 Step 1: Start Backend Application

Before testing any API, ensure your Spring Boot application is running.

```
http://localhost:8080
```

Verify:
- Application starts without errors
- Database connection is active
- Tables are created (User, RefreshToken)

---

### 🔐 Step 2: Execute Login APIs (Generate Tokens)

Start with authentication APIs:

#### ▶️ User Login
- Call `/api/auth/login` with user credentials
- On success, you will receive:
  - `accessToken` (for API access)
  - `refreshToken` (for session continuation)

#### ▶️ Admin Login
- Call `/api/auth/login` with admin credentials
- Generate separate:
  - `admin_token`
  - `admin_refresh_token`

---

### 📌 What Gets Stored in Postman

After login, Postman automatically stores:

- `user_token`
- `admin_token`
- `refresh_token`

These variables are used in all subsequent requests.

---

### ⚙️ Step 3: Execute Functional API Flows

Once authentication is complete, you can test all secured APIs:

#### 🔹 User Flow
- Access user profile APIs
- Validate JWT-based authorization
- Test role-based restrictions

#### 🔹 Admin Flow
- Access admin dashboard APIs
- Verify admin-only endpoints
- Validate role enforcement

#### 🔹 Security Validation Flow
- Test invalid token scenarios
- Test expired token scenarios
- Verify 401 / 403 responses

---

### 🔄 Step 4: Refresh Token Flow

When access token expires:

- Call `/api/auth/refresh`
- Pass `refresh_token` in request body
- Receive new `accessToken`
- Continue API usage without re-login

---

### 🚪 Step 5: Logout Flow

To terminate session:

- Call `/api/auth/logout`
- Refresh token is deleted from database
- Access token remains but becomes useless after expiry
- Any refresh attempt after logout fails with `401 Unauthorized`

---

## 📌 API Reference 

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
- ✔ Refresh token flow
- ✔ Logout flow

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

