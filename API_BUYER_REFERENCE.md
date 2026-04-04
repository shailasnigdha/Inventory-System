# API Endpoints Reference - Buyer Features

## Authentication Endpoints

### Get Current Authenticated User
**NEW ENDPOINT**

```http
GET /auth/me
```

**Description:** Returns information about the currently authenticated user.

**Authentication:** Required (any authenticated user)

**Response:** HTTP 200 OK
```json
{
  "id": 1,
  "username": "john_buyer",
  "email": "john@example.com",
  "roles": ["BUYER"]
}
```

**Error Responses:**
- `HTTP 401 Unauthorized` - User not authenticated
- `HTTP 404 Not Found` - User not found in system

**Use Case:** Used by the buyer dashboard to automatically retrieve the current user's ID for order operations.

---

## Order Management Endpoints

### Place a New Order
```http
POST /orders
Content-Type: application/json
```

**Description:** Create a new order for a product.

**Authentication:** Required (BUYER role)

**Request Body:**
```json
{
  "buyerId": 1,
  "productId": 2,
  "quantity": 1
}
```

**Parameters:**
- `buyerId` (Long, required) - ID of the buyer placing the order
- `productId` (Long, required) - ID of the product to order
- `quantity` (Integer, required) - Number of units to order (must be > 0)

**Response:** HTTP 201 Created
```json
{
  "id": 101,
  "buyerId": 1,
  "productId": 2,
  "quantity": 1,
  "status": "Pending",
  "createdAt": "2026-03-29T13:54:00"
}
```

**Error Responses:**
- `HTTP 400 Bad Request` - Invalid input data
- `HTTP 403 Forbidden` - User is not a BUYER
- `HTTP 404 Not Found` - Product or buyer not found
- `HTTP 409 Conflict` - Insufficient stock

---

### Get Orders for a Buyer
```http
GET /orders/buyer/{buyerId}
```

**Description:** Retrieve all orders placed by a specific buyer.

**Authentication:** Required (BUYER role for own orders, ADMIN for any)

**Path Parameters:**
- `buyerId` (Long, required) - ID of the buyer

**Response:** HTTP 200 OK
```json
[
  {
    "id": 101,
    "buyerId": 1,
    "productId": 2,
    "quantity": 1,
    "status": "Pending",
    "createdAt": "2026-03-29T13:54:00"
  },
  {
    "id": 102,
    "buyerId": 1,
    "productId": 3,
    "quantity": 2,
    "status": "Confirmed",
    "createdAt": "2026-03-29T14:00:00"
  }
]
```

**Error Responses:**
- `HTTP 403 Forbidden` - No permission to view these orders
- `HTTP 404 Not Found` - Buyer not found

---

### Get Single Order
```http
GET /orders/{orderId}
```

**Description:** Retrieve details of a specific order.

**Authentication:** Required (BUYER for own orders, ADMIN for any)

**Path Parameters:**
- `orderId` (Long, required) - ID of the order

**Response:** HTTP 200 OK
```json
{
  "id": 101,
  "buyerId": 1,
  "productId": 2,
  "quantity": 1,
  "status": "Pending",
  "createdAt": "2026-03-29T13:54:00"
}
```

**Error Responses:**
- `HTTP 403 Forbidden` - No permission to view this order
- `HTTP 404 Not Found` - Order not found

---

### Update an Order
```http
PUT /orders/{orderId}
Content-Type: application/json
```

**Description:** Update an existing order (e.g., change quantity).

**Authentication:** Required (BUYER role)

**Path Parameters:**
- `orderId` (Long, required) - ID of the order to update

**Request Body:**
```json
{
  "buyerId": 1,
  "productId": 2,
  "quantity": 3
}
```

**Response:** HTTP 200 OK
```json
{
  "id": 101,
  "buyerId": 1,
  "productId": 2,
  "quantity": 3,
  "status": "Pending",
  "createdAt": "2026-03-29T13:54:00"
}
```

**Error Responses:**
- `HTTP 400 Bad Request` - Invalid input data
- `HTTP 403 Forbidden` - User is not a BUYER or doesn't own the order
- `HTTP 404 Not Found` - Order not found

---

### Delete an Order
```http
DELETE /orders/{orderId}
```

**Description:** Cancel/delete an order.

**Authentication:** Required (BUYER for own orders, ADMIN for any)

**Path Parameters:**
- `orderId` (Long, required) - ID of the order to delete

**Response:** HTTP 204 No Content

**Error Responses:**
- `HTTP 403 Forbidden` - No permission to delete this order
- `HTTP 404 Not Found` - Order not found

---

## Product Endpoints

### Get All Products
```http
GET /products
```

**Description:** Retrieve all available products.

**Authentication:** Not required (public endpoint)

**Response:** HTTP 200 OK
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "sellerId": 2,
    "stock": 5,
    "createdAt": "2026-03-29T10:00:00"
  },
  {
    "id": 2,
    "name": "Mouse",
    "description": "Wireless mouse",
    "price": 25.99,
    "sellerId": 2,
    "stock": 50,
    "createdAt": "2026-03-29T10:05:00"
  }
]
```

---

### Get Single Product
```http
GET /products/{id}
```

**Description:** Retrieve details of a specific product.

**Authentication:** Not required (public endpoint)

**Path Parameters:**
- `id` (Long, required) - ID of the product

**Response:** HTTP 200 OK
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "sellerId": 2,
  "stock": 5,
  "createdAt": "2026-03-29T10:00:00"
}
```

**Error Responses:**
- `HTTP 404 Not Found` - Product not found

---

## cURL Examples for Testing

### Get Current User
```bash
curl -X GET http://localhost:8084/auth/me \
  -H "Content-Type: application/json" \
  --cookie "JSESSIONID=your_session_id"
```

### Get All Products
```bash
curl -X GET http://localhost:8084/products
```

### Place an Order
```bash
curl -X POST http://localhost:8084/orders \
  -H "Content-Type: application/json" \
  -d '{
    "buyerId": 1,
    "productId": 2,
    "quantity": 1
  }' \
  --cookie "JSESSIONID=your_session_id"
```

### Get Buyer Orders
```bash
curl -X GET http://localhost:8084/orders/buyer/1 \
  --cookie "JSESSIONID=your_session_id"
```

---

## Integration with Buyer Dashboard

The Buyer Dashboard automatically calls these endpoints:

1. **On Load:**
   - `GET /auth/me` - Get current buyer ID
   - `GET /products` - Load all available products
   - `GET /orders/buyer/{buyerId}` - Load buyer's order history

2. **On Order Placement:**
   - `POST /orders` - Submit new order
   - `GET /orders/buyer/{buyerId}` - Refresh order history

3. **On Product Selection:**
   - Product data is already cached from initial `/products` call

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Request successful |
| 201 | Created - Resource successfully created |
| 204 | No Content - Successful delete |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Access denied |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource state conflict |
| 500 | Internal Server Error - Server error |

---

## Authentication

All endpoints requiring authentication use Spring Security with session-based authentication.

**To authenticate:**
1. Make a login request to `/auth/login`
2. Session cookie is automatically set
3. Include cookie in subsequent requests

**Session Cookie:** `JSESSIONID`

---

## Rate Limiting

No rate limiting is currently implemented. Production deployments should consider adding rate limiting for API endpoints.

---

## Changelog

### Version 1.0 (2026-03-29)
- **New:** Added `GET /auth/me` endpoint
- **New:** Enhanced Buyer Dashboard with interactive features
- **New:** Order placement form with product selection
- **New:** Order history display with real-time updates
- **Enhanced:** Product browsing with stock information

