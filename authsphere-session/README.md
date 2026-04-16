### 🔐 AuthSphereSession - Spring Boot Security Project

AuthSphereSession is a Spring Boot-based authentication and authorization system implementing **session-based security with Spring Security** and **Redis distributed sessions**.

It demonstrates real-world backend security concepts like authentication, authorization, role-based access control, and session management.

---

### 🧠 Project Highlights

- Database authentication using MySQL + JPA
- Role-based access control (USER / ADMIN)
- BCrypt password encryption
- Custom login/logout flow using Spring Security
- Session-based authentication (JSESSIONID)
- Redis-based distributed session support (multi-instance login)
- Thymeleaf-based UI

---

### 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Redis (Spring Session)
- Maven

---

### 🛡️ Authentication Flow

1. User enters username & password on login page
2. Spring Security calls `CustomUserDetailsService`
3. User is fetched from MySQL database
4. Password is validated using BCrypt encoder
5. Roles are mapped to Spring authorities
6. On success:
   - ADMIN → `/admin`
   - USER → `/user`

---

### 📁 Project Structure
```text
AuthSphereSession/
├── src/main/java/com/authsphere/
│ ├── AuthSphereApplication.java → Main Spring Boot app
│
│ ├── controller/
│ │ └── ViewController.java → Handles login/user/admin pages
│
│ ├── security/
│ │ └── SecurityConfig.java → Spring Security configuration
│
│ ├── service/
│ │ └── CustomUserDetailsService.java → Loads user from DB
│
│ ├── model/
│ │ ├── User.java → User entity
│ │ └── Role.java → Role entity
│
│ ├── repository/
│ │ ├── UserRepository.java → User DB operations
│ │ └── RoleRepository.java → Role DB operations
│
├── src/main/resources/
│ ├── templates/
│ │ ├── login.html
│ │ ├── user.html
│ │ ├── admin.html
│ │ └── access-denied.html
│
│ ├── application.properties
│ └── data.sql
│
├── pom.xml
└── README.md
```
---

### 🔐 Authorization Rules

- `/admin` → only ROLE_ADMIN
- `/user` → ROLE_USER + ROLE_ADMIN

---

### 🚪 Session Management

- Default: Spring Security session stored in server memory
- With Redis:
  - Sessions stored centrally in Redis
  - Multiple application instances share same session
  - Restart-safe authentication

---

### ❌ Access Denied Handling

- 403 error redirects to custom access denied page
- API requests return JSON response

---

### 🔓 Logout Flow

- Session invalidation
- Authentication cleared
- JSESSIONID cookie removed
- Redirect to login page

---

### 🗄️ Database Schema

#### User Table
- id
- username
- password

#### Role Table
- id
- name (ROLE_USER, ROLE_ADMIN)

#### User_Roles Table
- user_id
- role_id

---

### 🧪 Initial Data (data.sql)

```sql
-- ===============================
-- Roles
-- ===============================
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

-- ===============================
-- Users (password = password123 BCrypt encoded)
-- ===============================
INSERT INTO user (username, password) VALUES 
('user', '$2a$10$gy/0VsvZpYDxmk4.fSjGReXibadroxnkFtX2KHgMXUv9qq9y9r0oi'),
('admin', '$2a$10$gy/0VsvZpYDxmk4.fSjGReXibadroxnkFtX2KHgMXUv9qq9y9r0oi');

-- ===============================
-- Role Mapping
-- ===============================
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1),
(2, 2);
```
---

### 🔑 Default Credentials

| Username | Password     | Role       |
|----------|-------------|------------|
| user     | password123 | ROLE_USER  |
| admin    | password123 | ROLE_ADMIN |

---

### 📌 Application Endpoints

| Endpoint | Description |
|----------|-------------|
| /login   | Login page |
| /user    | User dashboard |
| /admin   | Admin dashboard |
| /logout  | Logout endpoint |

### 🚀 How to Run AuthSphereSession

Follow the steps below to set up and run the AuthSphereSession project locally.

---

#### 1️. Clone the Repository
```bash
git clone https://github.com/your-username/AuthSphereSession.git
cd AuthSphereSession
```

#### 2. Configure MySQL Database

Create the database:

```sql
CREATE DATABASE authdb;
```

#### 3. Configure Application Properties

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/authdb
spring.datasource.username=root
spring.datasource.password=root
```


#### 4. Start Redis (Optional - for Distributed Session)

```bash
docker run -p 6379:6379 redis
```

#### 5. Build and Run the Application
```bash
mvn clean install
mvn spring-boot:run
```
---


### 🧪 Test the Application

After starting the application, verify the following:

---

#### Open Application
```text
http://localhost:8080/login
```
#### Login using Default Credentials
```text
user / password123 → USER dashboard
admin / password123 → ADMIN dashboard
```
#### Verify Role-Based Access
```text
/user → accessible by USER and ADMIN
/admin → accessible only by ADMIN
```
#### Test Session Behavior
```text
Login in one tab
Open another tab → session remains active (JSESSIONID)
```
#### Redis Test
```text
Run application on multiple instances
Verify same login works across instances using Redis session sharing
```
