# Inventory Management System

A role-based inventory management system built with Spring Boot, Spring Security, Thymeleaf, JPA, and PostgreSQL.

## Features

- Authentication with registration and login/logout
- Password encryption with BCrypt
- Role-based authorization (`ADMIN`, `SELLER`, `BUYER`)
- RESTful APIs for `Product` and `Order` (CRUD)
- Global exception handling with consistent error responses
- PostgreSQL persistence with related tables:
  - `users`
  - `roles`
  - `user_roles`
  - `products`
  - `orders`
- User-friendly web pages for login, registration, and role dashboards
- Unit tests (service layer) and controller integration tests (MockMvc + SpringBootTest)
- Dockerized app and PostgreSQL via Docker Compose

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- JUnit 5 + Mockito + MockMvc
- Docker

## Configuration

Environment variables (optional for local development, defaults provided):

- `DB_URL` (default: `jdbc:postgresql://localhost:5432/inventorydb`)
- `DB_USERNAME` (default: `postgres`)
- `DB_PASSWORD` (default: `S19151441s`)
- `SERVER_PORT` (optional, default `8084`)

For Docker Compose, create a `.env` file from `.env.example` and set your desired values.

## Local Run

### First Time Setup
See [DATABASE_SETUP.md](./DATABASE_SETUP.md) for PostgreSQL installation and configuration.

### Run Application
```powershell
Set-Location "d:\inventory"
.\mvnw.cmd spring-boot:run
```

Or use the convenient startup script:
```powershell
.\run-local.cmd
```

### With Custom Database Credentials
```powershell
Set-Location "d:\inventory"
$env:DB_URL="jdbc:postgresql://localhost:5432/inventorydb"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="S19151441s"
.\mvnw.cmd spring-boot:run
```

## Test

```powershell
Set-Location "d:\inventory"
.\mvnw.cmd test
```

## Docker Run

```powershell
Set-Location "d:\inventory"
Copy-Item .env.example .env
# edit .env values before first run
docker compose up --build
```

## Useful API Endpoints

- Auth:
  - `POST /auth/register/{role}`
  - `POST /auth/login`
- Products:
  - `POST /products` (SELLER)
  - `PUT /products/{id}` (SELLER/ADMIN)
  - `DELETE /products/{id}` (ADMIN)
  - `GET /products`
  - `GET /products/{id}`
  - `GET /products/seller/{sellerId}`
- Orders:
  - `POST /orders` (BUYER)
  - `PUT /orders/{orderId}` (BUYER)
  - `DELETE /orders/{orderId}` (BUYER/ADMIN)
  - `GET /orders/{orderId}` (BUYER/ADMIN)
  - `GET /orders` (ADMIN)
  - `GET /orders/buyer/{buyerId}` (BUYER/ADMIN)

