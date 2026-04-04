# Buyer Dashboard - Feature Documentation

## Overview
The Buyer Dashboard provides a comprehensive interface for users with the BUYER role to:
- Browse available products
- Place new orders
- Track order history

## Features

### 1. Browse Products
**Display:** Interactive product grid showing:
- Product name
- Description
- Seller ID
- Price
- Stock availability

**Functionality:**
- Displays all available products from all sellers
- Shows real-time stock status with color coding:
  - Green: Stock available
  - Red: Out of stock
- Products with zero stock are disabled in the order form

**API Endpoint Used:**
```
GET /products
```

---

### 2. Place a New Order
**Display:** Form with the following fields:
- **Select Product** (Dropdown) - Lists all available products with current stock and price
- **Quantity** (Number input) - Default: 1, minimum: 1

**Functionality:**
- Form is dynamically populated from the products list
- Validates that quantity is at least 1
- Shows success/error messages after submission
- Automatically refreshes order history after successful order

**API Endpoint Used:**
```
POST /orders
Content-Type: application/json

{
  "buyerId": <current_user_id>,
  "productId": <selected_product_id>,
  "quantity": <quantity_value>
}
```

**Response:** Returns the created Order object with:
- Order ID
- Buyer ID
- Product ID
- Quantity
- Status
- Created timestamp

**Error Handling:**
- Invalid product ID
- Insufficient stock
- Missing required fields
- Authorization errors (must be BUYER role)

---

### 3. Track Orders
**Display:** Table showing order history with columns:
- **Order ID** - Unique order identifier
- **Product ID** - Referenced product
- **Quantity** - Number of units ordered
- **Order Date** - Date in local format (YYYY-MM-DD)
- **Status** - Current order status with color-coded badge

**Status Badges:**
- Pending (Yellow): Order placed, awaiting confirmation
- Confirmed (Blue): Order confirmed
- Shipped (Light Blue): Order is in transit
- Other statuses as configured in the system

**Functionality:**
- Shows all orders for the current buyer only
- Displays empty state if no orders have been placed
- Updates automatically after placing new order
- Read-only display (view orders only)

**API Endpoint Used:**
```
GET /orders/buyer/{buyerId}
```

**Response:** Returns array of Order objects for the specified buyer:
```json
[
  {
    "id": 1,
    "buyerId": 1,
    "productId": 2,
    "quantity": 1,
    "status": "Pending",
    "createdAt": "2026-03-29T13:54:00"
  }
]
```

---

## User Experience

### On Page Load
1. Fetches current user information from `/auth/me`
2. Extracts buyer ID from authenticated user
3. Loads all available products
4. Populates the order form dropdown
5. Loads buyer's order history

### Placing an Order
1. User selects a product from dropdown
2. Enters desired quantity
3. Clicks "Place Order" button
4. System sends POST request to `/orders`
5. On success: Shows success message, resets form, refreshes order history
6. On error: Shows error message with details

### Real-time Updates
- Order history refreshes automatically after successful order placement
- Products list shows current stock information
- All data is fetched fresh on each interaction

---

## Security

### Role-Based Access
- **Required Role:** BUYER
- **Endpoint Access Control:**
  - `POST /orders` - BUYER only
  - `GET /products` - Public (but requires authentication in most cases)
  - `GET /orders/buyer/{buyerId}` - BUYER and ADMIN
  - `GET /auth/me` - Authenticated users only

### Authentication
- Session-based authentication via Spring Security
- Current user information retrieved via `/auth/me` endpoint
- User ID automatically extracted and used for all operations

---

## Technical Details

### Frontend
- **Framework:** Vanilla JavaScript (no jQuery/frameworks required)
- **Styling:** CSS Grid and Flexbox for responsive layout
- **API Communication:** Fetch API
- **Loading States:** Spinner animations during data fetches
- **Error Handling:** Try-catch blocks with user-friendly error messages

### Backend
- **Framework:** Spring Boot REST API
- **Security:** Spring Security with role-based access control
- **Database:** JPA/Hibernate with PostgreSQL
- **Validation:** Jakarta Validation annotations

### Endpoints Used

#### Get Current User
```
GET /auth/me
Response: 
{
  "id": 1,
  "username": "john_buyer",
  "email": "john@example.com",
  "roles": ["BUYER"]
}
```

#### Get All Products
```
GET /products
Response: [
  {
    "id": 1,
    "name": "Product Name",
    "description": "Description",
    "price": 99.99,
    "sellerId": 2,
    "stock": 10,
    "createdAt": "2026-03-29T10:00:00"
  }
]
```

#### Place Order
```
POST /orders
Content-Type: application/json

{
  "buyerId": 1,
  "productId": 2,
  "quantity": 1
}

Response: 
{
  "id": 101,
  "buyerId": 1,
  "productId": 2,
  "quantity": 1,
  "status": "Pending",
  "createdAt": "2026-03-29T13:54:00"
}
```

#### Get Buyer Orders
```
GET /orders/buyer/1
Response: [
  {
    "id": 101,
    "buyerId": 1,
    "productId": 2,
    "quantity": 1,
    "status": "Pending",
    "createdAt": "2026-03-29T13:54:00"
  }
]
```

---

## How to Use

### 1. Access the Dashboard
- Navigate to the application dashboard after logging in as a BUYER
- Click "Buyer Dashboard" or access `/dashboard/buyer`

### 2. Browse Products
- The products grid loads automatically
- See all available products with prices and stock information

### 3. Place an Order
- Select a product from the dropdown
- Enter the desired quantity
- Click "Place Order"
- Confirmation message appears on success

### 4. Track Orders
- View your complete order history in the table below
- See order status and dates for all your orders

### 5. Logout
- Click the "Logout" button in the top right corner

---

## Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| "Failed to load products" | Server error or network issue | Refresh the page |
| "Failed to load form" | Error fetching product list | Refresh the page |
| "Failed to load order history" | Server error or authorization issue | Refresh the page |
| "Error placing order" | Invalid data or server error | Check input and try again |
| "Please select a product" | No product selected | Select a product from dropdown |
| "User not authenticated" | Session expired | Log back in |

---

## Testing the Buyer Dashboard

### Example Test Scenario
```
1. Register as a BUYER user
   - Endpoint: POST /auth/register/BUYER
   - Data: {"username":"john_buyer","email":"john@example.com","password":"pass123"}

2. Login
   - Navigate to /login or login page
   - Enter credentials

3. Access Buyer Dashboard
   - Click "Buyer Dashboard" or navigate to /dashboard/buyer

4. Browse Products
   - Verify products are displayed
   - Check prices and stock information

5. Place an Order
   - Select a product from the dropdown
   - Enter quantity "2"
   - Click "Place Order"
   - Verify success message

6. Track Orders
   - Verify your new order appears in the history table
   - Check order details are correct

7. Verify Order History
   - Navigate to /orders/buyer/{buyerId} API endpoint
   - Confirm order is listed
```

---

## Related Dashboards

- **Admin Dashboard** (`/dashboard/admin`) - Manage all orders and products
- **Seller Dashboard** (`/dashboard/seller`) - List and manage your products
- **Login Page** (`/login`) - Authenticate as a BUYER user

---

## Support

For issues or questions:
1. Check error messages displayed on the dashboard
2. Review browser console (F12) for JavaScript errors
3. Check server logs for API errors
4. Verify user has BUYER role
5. Ensure all required endpoints are accessible

