# Seller Dashboard - Feature Documentation

## Overview
The Seller Dashboard provides a comprehensive interface for users with the SELLER role to:
- Manage and monitor inventory statistics
- Create new products
- Update existing product information (price, stock, description)
- Delete products
- View complete product catalog

## Features

### 1. Inventory Statistics Dashboard
**Display:** Overview cards showing key metrics:
- **Total Products** - Count of all products listed by seller
- **Total Stock** - Sum of all product quantities
- **Total Value** - Total inventory value (price × quantity)

**Functionality:**
- Real-time calculation of metrics
- Updates automatically after product changes
- Color-coded cards for visual distinction
- Displays with icons for quick recognition

**API Endpoint Used:**
```
GET /products/seller/{sellerId}
```

---

### 2. Create New Product
**Display:** Form with the following fields:
- **Product Name** (Text) - Required, product identifier
- **Price** (Number) - Required, must be > 0, supports decimals
- **Quantity** (Number) - Required, must be ≥ 0
- **Description** (Textarea) - Optional, product details

**Functionality:**
- Clean form layout with organized fields
- Input validation on client side
- Immediate feedback on submission
- Form auto-clears on successful creation
- Shows success/error messages
- Updates product catalog automatically

**API Endpoint Used:**
```
POST /products
Content-Type: application/json

{
  "name": "Product Name",
  "description": "Product details",
  "price": 99.99,
  "stock": 10,
  "sellerId": <current_seller_id>
}
```

**Response:** Returns created Product object with:
- Product ID
- Name, description, price, stock
- Seller ID
- Created timestamp

**Error Handling:**
- Missing required fields
- Invalid price (negative or zero)
- Invalid stock (negative)
- Authorization errors (must be SELLER role)
- Server validation errors

---

### 3. Product Catalog Management
**Display:** Table showing all seller's products with columns:
- **Name** - Product name (bold for emphasis)
- **Description** - Product details (shows "N/A" if empty)
- **Price** - Product price formatted as currency
- **Stock** - Current quantity with color indicator
- **Value** - Product value (price × stock)
- **Actions** - Edit button for modifications

**Functionality:**
- Lists all products for current seller only
- Color-coded stock status:
  - Green background: Stock available
  - Red background: Out of stock
- Displays empty state when no products
- Real-time updates after changes

**API Endpoint Used:**
```
GET /products/seller/{sellerId}
```

---

### 4. Edit Product Inline
**Display:** Expandable edit form for each product showing:
- **Product Name** - Edit current name
- **Price** - Edit current price
- **Quantity** - Edit current stock level
- **Description** - Edit product details

**Functionality:**
- Click "Edit" button on any product to expand edit form
- Inline editing without page navigation
- Pre-populated fields with current values
- Three actions: Update, Cancel, Delete
- Form validates before submission
- Automatically closes and refreshes on success

**API Endpoint Used:**
```
PUT /products/{productId}
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated details",
  "price": 119.99,
  "stock": 20,
  "sellerId": <current_seller_id>
}
```

**Error Handling:**
- Invalid input values
- Product not found
- Authorization errors
- Server-side validation errors

---

### 5. Delete Product
**Functionality:**
- Available via edit form "Delete" button
- Confirmation dialog before deletion
- Removes product permanently
- Updates catalog immediately
- Shows success/error message

**API Endpoint Used:**
```
DELETE /products/{productId}
```

**Access Control:**
- Sellers can delete their own products
- Admins can delete any product
- Other users: Access denied (403 Forbidden)

---

## User Experience

### On Page Load
1. Fetches current user information from `/auth/me`
2. Extracts seller ID from authenticated user
3. Loads all seller's products
4. Calculates and displays statistics
5. Initializes product create form

### Creating a Product
1. User fills form with product details
2. System validates inputs
3. Submits POST /products request
4. On success: Shows message, clears form, refreshes catalog and stats
5. On error: Shows error message with details

### Managing Products
1. User sees complete product list
2. Can click "Edit" on any product
3. Edit form expands inline
4. Can update name, price, quantity, description
5. Submit to save changes
6. Or click "Delete" to remove permanently
7. Catalog updates automatically

### Real-time Updates
- Statistics recalculate after any change
- Product list refreshes without page reload
- New products appear immediately
- Updated products show latest data

---

## Security

### Role-Based Access
- **Required Role:** SELLER
- **Endpoint Access Control:**
  - `POST /products` - SELLER only
  - `PUT /products/{id}` - SELLER (own) or ADMIN
  - `DELETE /products/{id}` - ADMIN only (or SELLER for own via controller rules)
  - `GET /products/seller/{id}` - SELLER (own) or ADMIN
  - `GET /auth/me` - Authenticated users only

### Authentication
- Session-based authentication via Spring Security
- Current user information retrieved via `/auth/me` endpoint
- User ID automatically extracted and used for all operations
- Seller can only manage own products

---

## Technical Details

### Frontend
- **Framework:** Vanilla JavaScript (no external dependencies)
- **Styling:** CSS Grid and Flexbox for responsive layout
- **API Communication:** Fetch API
- **Loading States:** Spinner animations during data fetches
- **Error Handling:** Try-catch blocks with user-friendly messages
- **Real-time Stats:** Calculated from product data

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
  "id": 2,
  "username": "john_seller",
  "email": "seller@example.com",
  "roles": ["SELLER"]
}
```

#### Create Product
```
POST /products
Content-Type: application/json

{
  "name": "Laptop Pro",
  "description": "High-performance laptop",
  "price": 999.99,
  "stock": 5,
  "sellerId": 2
}

Response: 
{
  "id": 1,
  "name": "Laptop Pro",
  "description": "High-performance laptop",
  "price": 999.99,
  "stock": 5,
  "sellerId": 2,
  "createdAt": "2026-03-29T14:00:00"
}
```

#### Get Seller's Products
```
GET /products/seller/2
Response: [
  {
    "id": 1,
    "name": "Laptop Pro",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 5,
    "sellerId": 2,
    "createdAt": "2026-03-29T14:00:00"
  }
]
```

#### Update Product
```
PUT /products/1
Content-Type: application/json

{
  "name": "Laptop Pro Max",
  "description": "Updated description",
  "price": 1199.99,
  "stock": 3,
  "sellerId": 2
}

Response: 
{
  "id": 1,
  "name": "Laptop Pro Max",
  "description": "Updated description",
  "price": 1199.99,
  "stock": 3,
  "sellerId": 2,
  "createdAt": "2026-03-29T14:00:00"
}
```

#### Delete Product
```
DELETE /products/1
Response: HTTP 204 No Content
```

---

## Testing the Seller Dashboard

### Example Test Scenario
```
1. Register as a SELLER user
   - Endpoint: POST /auth/register/SELLER
   - Data: {"username":"john_seller","email":"seller@example.com","password":"pass123"}

2. Login
   - Navigate to /login or login page
   - Enter credentials

3. Access Seller Dashboard
   - Click "Seller Dashboard" or navigate to /dashboard/seller

4. Check Inventory Stats
   - Verify stats show correctly (0 products initially)

5. Create a Product
   - Name: "Laptop"
   - Price: 999.99
   - Quantity: 5
   - Description: "High-performance laptop"
   - Click "Create Product"
   - Verify success message
   - Check product appears in catalog
   - Verify stats updated (1 product, 5 stock, $4999.95 value)

6. Edit a Product
   - Click "Edit" on the product
   - Change price to 1199.99
   - Change quantity to 3
   - Click "Update"
   - Verify product updated in table
   - Verify stats updated ($3599.97 value)

7. Delete a Product
   - Click "Delete" button
   - Confirm deletion
   - Verify product removed
   - Verify stats reset

8. Verify Product API
   - Navigate to /products/seller/{sellerId} API endpoint
   - Confirm products are listed
```

---

## Related Dashboards

- **Buyer Dashboard** (`/dashboard/buyer`) - Browse and order products
- **Admin Dashboard** (`/dashboard/admin`) - Manage all products and orders
- **Login Page** (`/login`) - Authenticate as a SELLER user

---

## Support

For issues or questions:
1. Check error messages displayed on the dashboard
2. Review browser console (F12) for JavaScript errors
3. Check server logs for API errors
4. Verify user has SELLER role
5. Ensure all required endpoints are accessible

