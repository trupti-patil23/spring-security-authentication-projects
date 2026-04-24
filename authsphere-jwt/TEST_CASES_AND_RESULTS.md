# 🧪 AuthSphere JWT – Test Cases & Results

This document contains **end-to-end API test scenarios** along with **actual responses**, including HTTP status codes and error messages.

---

# 🔐 1. AUTHENTICATION (REGISTER & LOGIN)

---

## ✅ TC-01: Register User (Success)

**Request**

```http
POST /api/auth/register
```

```json
{
  "username": "john",
  "password": "Password@123"
}
```

**Response**

```http
200 OK
```

```json
{
  "message": "User registered successfully"
}
```

---

## ❌ TC-02: Register Duplicate User

**Request**

```json
{
  "username": "john",
  "password": "Password@123"
}
```

**Response**

```http
409 CONFLICT
```

```json
{
  "message": "User already exists: john"
}
```
---

## ❌ TC-03: Register Validation Error

**Request**

```json
{
  "username": "",
  "password": ""
}
```

**Response**

```http
400 BAD REQUEST
```

```json
{
    "password": "Password cannot be empty",
    "username": "Username cannot be empty"
}
```
---

## ❌ TC-04: Register Validation Error

**Request**

```json
{
  "username": "",
  "password": "123"
}
```

**Response**

```http
400 BAD REQUEST
```

```json
{
    "password": "Password must be at least 8 characters long and contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character",
    "username": "Username cannot be empty"
}
```

---

## ✅ TC-05: Login Success

**Request**

```http
POST /api/auth/login
```

```json
{
  "username": "user",
  "password": "Password@123"
}
```

**Response**

```http
200 OK
```

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzc3MDUxNTcxLCJleHAiOjE3NzcwNTUxNzF9.pgMWi8ApRMdA1vtX3Q3nfqrEmfyUiHNbIKNp6LaEB1w"
}
```

---

## ❌ TC-06: Login Wrong Password

**Request**

```json
{
  "username": "user",
  "password": "wrong"
}
```

**Response**

```http
401 UNAUTHORIZED
```

```json
{
    "error": "Unauthorized",
    "message": "Invalid username or password",
    "status": 401
}
```

---

## ❌ TC-07: Login User Not Found

**Request**

```json
{
  "username": "unknown",
  "password": "Password@123"
}
```

**Response**

```http
401 UNAUTHORIZED
```

```json
{
    "error": "Unauthorized",
    "message": "Invalid username or password",
    "status": 401
}
```

---

# 🔐 2. JWT AUTHENTICATION

---

## ❌ TC-08: Access Without Token

**Request**

```http
GET /api/user/profile
```

**Response**

```http
401 UNAUTHORIZED
```

```json
{
    "status": 401,
    "error": "Unauthorized",
    "message": "Authentication token is missing"
}
```

---

## ❌ TC-09: Access With Invalid Token

**Request**

```http
GET /api/user/profile
Authorization: Bearer <invalid token>
```

**Response**

```http
401 UNAUTHORIZED
```

```json
{
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid or expired JWT token"
}
```

---

## ❌ TC-10: Access With Expired Token

**Response**

```http
401 UNAUTHORIZED
```

```json
{
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid or expired JWT token"
}
```

---

## ✅ TC-11: Access With Valid Token

**Request**

```http
GET /api/user/profile
Authorization: Bearer <User valid_token>
```

**Response**

```http
200 OK
```

```text
User profile data - Access granted
```

---

# 🔐 3. ROLE-BASED AUTHORIZATION

---

## ✅ TC-12: USER → USER API

**Request**

```http
GET /api/user/profile
Authorization: Bearer <user_token>
```

**Response**

```http
200 OK
```

```text
User profile data - Access granted
```

---

## ❌ TC-13: USER → ADMIN API

**Request**

```http
GET /api/admin/dashboard
Authorization: Bearer <user_token>
```

**Response**

```http
403 FORBIDDEN
```

```json
{
    "error": "Forbidden - You do not have permission to access this resource"
}
```

---

## ❌ TC-14: ADMIN API Without Token

**Request**

```http
GET /api/admin/dashboard
```

**Response**

```http
401 UNAUTHORIZED
```
```text
{
    "status": 401,
    "error": "Unauthorized",
    "message": "Authentication token is missing"
}
```
---

## ✅ TC-15: ADMIN - Login Success

**Request**

```http
POST /api/auth/login
```

```json
{
  "username": "admin",
  "password": "Password@123"
}
```

**Response**

```http
200 OK
```

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc3NzA1MjM4OSwiZXhwIjoxNzc3MDU1OTg5fQ.T5Bb-9d3kKz7-q-axU8fyBDiCUfbo3WqaJV9PF9AX94"
}
```

---

## ❌ TC-16: Access With Invalid Token

**Request**

```http
GET  /api/admin/dashboard
Authorization: Bearer <invalid token>
```

**Response**

```http
401 UNAUTHORIZED
```

```json
{
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid or expired JWT token"
}
```

---

## ✅ TC-17: ADMIN → ADMIN API

**Request**

```http
GET /api/admin/dashboard
Authorization: Bearer <admin_token>
```

**Response**

```http
200 OK
```

```text
Admin dashboard - Access granted
```
---

## ✅ TC-18: ADMIN → User API

**Request**

```http
GET api/user/profile
Authorization: Bearer <admin_token>
```

**Response**

```http
200 OK
```

```text
User profile data - Access granted
```

---

# 🧠 STATUS CODE SUMMARY

| Status Code | Meaning                   |
| ----------- | ------------------------- |
| 200         | Success                   |
| 400         | Validation Error          |
| 401         | Authentication Failure    |
| 403         | Authorization Failure     |
| 409         | Conflict (Duplicate User) |

---

# 🔐 NOTES

* JWT must be sent in header:

```text
Authorization: Bearer <token>
```

* Tokens expire based on configured expiry time

* Roles used:

  * `ROLE_USER`
  * `ROLE_ADMIN`

---
