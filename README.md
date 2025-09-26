# 📦 Product Comparison API - MercadoLibre Challenge

A simplified backend API built with **Spring Boot** that supplies product details for use in an **item comparison feature**.  
The API is designed following **RESTful best practices**, includes **CRUD operations**, **validation & error handling**, and persists data in a local `products.json` file.

---

## 🚀 Features

- Retrieve all products or a single product by ID
- Compare multiple products by IDs
- Create new products with validation (name required, rating between 0–5, etc.)
- Update existing products
- Delete products
- Centralized error handling with meaningful HTTP status codes

---

## 📂 Project Structure  

```
src/main/java/mfondini/meli/challenge
│
├── controller # REST controllers (API endpoints)
├── service # Business logic
├── repository # File-based persistence (JSON)
├── model # Domain models + validation annotations
├── exception # Custom exceptions + GlobalExceptionHandler
└── Application.java # Spring Boot main class

```
---

## 🌐 API Endpoints

### 🔹 Products

| Method   | Endpoint             | Description                        | Request Body Example |
|----------|----------------------|------------------------------------|----------------------|
| `GET`    | `/products`          | Get all products                   | – |
| `GET`    | `/products/{id}`     | Get a single product by ID         | – |
| `POST`   | `/products`          | Create a new product               | `{ "name": "Laptop X", "price": 1200, "rating": 4.5, "imageUrl": "...", "description": "...", "specifications": {"cpu":"i7","ram":"16GB"} }` |
| `PUT`    | `/products/{id}`     | Update an existing product         | Same as `POST` body |
| `DELETE` | `/products/{id}`     | Delete a product                   | – |
| `POST`   | `/products/compare`  | Compare multiple products by IDs   | `[1,2]` |

---

## ⚠️ Error Handling

All errors are returned in a consistent JSON structure:

```json
{
  "timestamp": "2025-09-26T12:30:35.565102287",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "name": "Name cannot be empty",
    "rating": "must be between 0 and 5"
  },
  "path": "/products"
}
```
* 404 Not Found → When product ID doesn’t exist
* 400 Bad Request → Validation errors (e.g., empty name, invalid rating)
* 500 Internal Server Error → Unexpected errors

---
## 🛠️ Setup Instructions
### Requirements
* Java 17+
* Maven 3+

### Clone repo
```
git clone https://github.com/Fondi99/meli-challenge.git
```
### Run tests
```
mvn test
```
### Start app
```
mvn spring-boot:run
```

---


## 🗂️ Sample Data
### The application starts with 5 preloaded products (stored in products.json).

```json
{
    "id": 1,
    "name": "Laptop Air 13",
    "imageUrl": "https://example.com/laptop-air.jpg",
    "description": "Lightweight and portable laptop.",
    "price": 899.99,
    "rating": 4.3,
    "specifications": {
        "cpu": "Intel i5",
        "ram": "8GB",
        "storage": "256GB SSD",
        "screen": "13-inch Retina"
    }
}

```

---

## 🏗️ Architectural Decisions

* Layered Architecture: separated concerns (Controller → Service → Repository).
* File-based Persistence: JSON instead of a DB for simplicity. Could easily be replaced with JPA + DB.
* Validation: using jakarta.validation annotations (@NotBlank, @Min, @Max).
* Error Handling: centralized with @ControllerAdvice.
* Extensibility: ProductComparisonRequest allows adding more comparison options in the future.