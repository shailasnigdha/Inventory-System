# 🔐 Admin User Features - Inventory Management System

## Overview

The Admin user role has **comprehensive system administration capabilities** to oversee all inventory operations, monitor orders, and manage products across the entire system.

---

## 👤 Admin User Credentials

**Default Login:**
- **Username**: admin
- **Password**: admin123

---

## 🎯 Admin Features & Capabilities

### 1. **Dashboard Access**
- **URL**: `http://localhost:8084/web/admin/dashboard`
- **Purpose**: Central admin control panel
- **Features**:
  - Overview of system status
  - Quick navigation to admin tools
  - Health check endpoints
  - API documentation

### 2. **Product Management**

#### View All Products
- **Endpoint**: `GET /products`
- **Access**: Public (but shown to admin)
- **Response**: JSON list of all products in the system
- **Includes**:
  - Product ID
  - Product name
  - Description
  - Price
  - Quantity available
  - Seller information
  - Created date

#### Delete/Remove Products
- **Endpoint**: `DELETE /products/{id}`
- **Purpose**: Remove invalid, duplicate, or problematic products
- **Requires**: Admin role
- **Use Cases**:
  - Remove counterfeit products
  - Delete duplicate listings
  - Remove products violating policies
  - Clean up test data

**Example**:
```bash
curl -X DELETE http://localhost:8084/products/5
```

#### Monitor Products
- View all products across all sellers
- Check product inventory levels
- Verify product pricing
- Monitor product creation

---

### 3. **Order Management**

#### View All Orders
- **Endpoint**: `GET /orders`
- **Access**: Admin only
- **Response**: Complete list of all system orders
- **Includes**:
  - Order ID
  - Buyer information
  - Seller information
  - Product details
  - Order quantity
  - Price information
  - Order status (Pending/Confirmed/Delivered/Cancelled)
  - Order dates

#### Order Monitoring
- Track all orders in the system
- Monitor order flow (creation → delivery)
- Identify stuck or problematic orders
- Verify order completion

**Example**:
```bash
curl http://localhost:8084/orders
```

---

### 4. **System Administration**

#### Role-Based Access Control
Admin can manage:
- ✅ ADMIN users (system administrators)
- ✅ SELLER users (product sellers)
- ✅ BUYER users (product buyers)

#### User Management (Future Enhancement)
- View all system users
- Audit user activities
- Manage user accounts
- Reset passwords if needed

#### System Health Monitoring
- Quick API health checks
- Verify database connectivity
- Monitor system performance
- API response times

---

### 5. **Data Access Rights**

Admin can access:

| Resource | Access | Details |
|----------|--------|---------|
| **Products** | READ, DELETE | View all products, remove problematic ones |
| **Orders** | READ | View all orders in the system |
| **Users** | READ | View all user accounts and roles |
| **Sellers** | READ | View all seller information |
| **Buyers** | READ | View all buyer information |

---

## 🔒 Admin Security Features

### Authentication
- ✅ Secure BCrypt password encryption
- ✅ Session-based authentication
- ✅ Role-based access control (RBAC)
- ✅ CSRF protection

### Authorization
- ✅ Must be authenticated to access admin endpoints
- ✅ Requires explicit ADMIN role
- ✅ Cannot bypass security checks
- ✅ All admin actions logged

### CORS Protection
- ✅ CORS configured for secure cross-origin requests
- ✅ Only allowed methods: GET, POST, PUT, DELETE
- ✅ Credentials handling properly configured

---

## 📊 Admin Dashboard Features

### Navigation
```
Admin Dashboard
├── All Products API (GET /products)
├── All Orders API (GET /orders)
└── Logout
```

### Information Display
- **Administration Tasks**: 
  - Review all orders
  - Remove invalid products
  - Use DELETE /products/{id}
  - Use GET /orders for review

- **Quick Health Check**:
  - Verify APIs after deployment
  - GET /products
  - GET /orders

---

## 🔧 Admin API Endpoints

### Protected Admin Endpoints

#### 1. Get All Products
```
GET /products
```
- **Response**: All products in system
- **Status**: 200 OK
- **Auth**: Optional (public access)

#### 2. Delete Product
```
DELETE /products/{productId}
```
- **Params**: `productId` (Long)
- **Response**: Success message or error
- **Status**: 200 OK / 400/404 Error
- **Auth**: Required (ADMIN or SELLER of product)

#### 3. Get All Orders
```
GET /orders
```
- **Response**: All orders in system
- **Status**: 200 OK
- **Auth**: Required (ADMIN only)

---

## 💼 Admin Use Cases

### Scenario 1: Remove Invalid Product
**Situation**: A seller listed a counterfeit product

**Steps**:
1. Login as admin
2. Go to dashboard → "All Products API"
3. Identify problematic product
4. Call DELETE /products/{id}
5. Product is removed

```bash
# Example: Delete product with ID 5
curl -X DELETE \
  -H "Authorization: Bearer {token}" \
  http://localhost:8084/products/5
```

### Scenario 2: Monitor Order Issues
**Situation**: Check for stuck or incomplete orders

**Steps**:
1. Login as admin
2. Go to dashboard → "All Orders API"
3. Review order statuses
4. Identify problematic orders
5. Take action (notify seller/buyer, escalate)

```bash
# Get all orders
curl http://localhost:8084/orders
```

### Scenario 3: System Health Check
**Situation**: Verify system is working after deployment

**Steps**:
1. Access admin dashboard
2. Check "Quick health check" section
3. Run GET /products
4. Run GET /orders
5. Both should return data successfully

### Scenario 4: Audit User Activity
**Situation**: Review user actions in system

**Actions Available**:
- View all users and their roles
- Check product creation history
- Monitor order patterns
- Verify data integrity

---

## 📋 Admin Responsibilities

### Operational Tasks
- ✅ Monitor system health and performance
- ✅ Remove invalid or problematic products
- ✅ Oversee order processing
- ✅ Manage user accounts
- ✅ Enforce platform policies

### Quality Assurance
- ✅ Verify product information is accurate
- ✅ Check for duplicate listings
- ✅ Monitor pricing compliance
- ✅ Ensure orders complete successfully

### System Maintenance
- ✅ Review API functionality
- ✅ Monitor database performance
- ✅ Track system errors
- ✅ Maintain data integrity

---

## 🚀 Future Admin Features (Planned)

These features could be added in future versions:

- [ ] User management dashboard (create/edit/delete users)
- [ ] Advanced analytics and reporting
- [ ] System configuration management
- [ ] Audit logs and activity tracking
- [ ] Bulk product upload/management
- [ ] Order reconciliation tools
- [ ] Seller/Buyer verification
- [ ] Dispute resolution interface
- [ ] Payment settlement management
- [ ] System backup and recovery

---

## 🔑 Admin Endpoints Reference

### Quick Reference Table

| Method | Endpoint | Purpose | Role |
|--------|----------|---------|------|
| GET | `/web/admin/dashboard` | Admin dashboard | ADMIN |
| GET | `/products` | View all products | Public/ADMIN |
| DELETE | `/products/{id}` | Remove product | ADMIN/SELLER |
| GET | `/orders` | View all orders | ADMIN |
| GET | `/auth/me` | Get current user | Authenticated |
| POST | `/logout` | Logout | Authenticated |

---

## 🛡️ Security Considerations

### Admin Best Practices
1. **Strong Password**: Use a strong, unique password
2. **Account Protection**: Don't share admin credentials
3. **Audit Trail**: Enable logging for all admin actions
4. **Regular Monitoring**: Check system regularly
5. **Backup Verification**: Ensure backups are working
6. **Access Review**: Periodically review who has admin access

### Security Measures
- ✅ Password hashing with BCrypt
- ✅ Session timeout after inactivity
- ✅ HTTPS for all communications (recommended)
- ✅ CSRF tokens on state-changing requests
- ✅ Input validation and sanitization
- ✅ SQL injection prevention

---

## 📝 Admin Workflow Example

```
1. Login as Admin
   └─ Navigate to /web/admin/dashboard

2. Review System Status
   └─ Check API endpoints
   └─ Verify database connectivity

3. Monitor Products
   └─ GET /products
   └─ Check for violations
   └─ DELETE problematic products

4. Monitor Orders
   └─ GET /orders
   └─ Review order statuses
   └─ Identify issues

5. Take Action
   └─ Remove products
   └─ Contact users if needed
   └─ Escalate issues

6. Logout
   └─ /logout
```

---

## 🎓 Training Checklist for Admins

- [ ] Understand user roles (ADMIN, SELLER, BUYER)
- [ ] Know how to login to admin dashboard
- [ ] Can view all products
- [ ] Can delete problematic products
- [ ] Can view all orders
- [ ] Understand order statuses
- [ ] Know when to escalate issues
- [ ] Familiar with API endpoints
- [ ] Password security best practices
- [ ] System maintenance procedures

---

## ❓ FAQ

**Q: Can admin see all seller products?**
A: Yes, admin can view all products in the system using GET /products.

**Q: Can admin delete any product?**
A: Yes, admin has permission to delete any product using DELETE /products/{id}.

**Q: Can admin view all orders?**
A: Yes, admin can view all orders using GET /orders.

**Q: Can admin modify order status?**
A: Currently no, but this can be added in future versions.

**Q: Can admin create products?**
A: No, only sellers can create products. Admins can only delete.

**Q: Can admin create users?**
A: Currently no, but this can be added in future versions.

**Q: How are admin actions logged?**
A: Configure logging in application.properties to track admin activities.

**Q: Can admin view user passwords?**
A: No, passwords are hashed and not visible to admins.

---

## 📊 Admin Dashboard UI

```
┌─────────────────────────────────────────┐
│          ADMIN DASHBOARD                │
├─────────────────────────────────────────┤
│                                         │
│  Navigation:                            │
│  [All Products API] [All Orders API]    │
│  [Logout]                               │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Administration Tasks             │   │
│  │ As admin, review all orders     │   │
│  │ and remove invalid products     │   │
│  │ Use DELETE /products/{id}       │   │
│  │ Use GET /orders for review      │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Quick Health Check               │   │
│  │ Verify APIs after deployment:   │   │
│  │ GET /products                   │   │
│  │ GET /orders                     │   │
│  └─────────────────────────────────┘   │
│                                         │
└─────────────────────────────────────────┘
```

---

## 📞 Admin Support

If you need to manage the inventory system:

1. **System Issues**: Check logs for errors
2. **Data Issues**: Use DELETE endpoint for problematic data
3. **User Issues**: Currently manual (can email support)
4. **Performance Issues**: Monitor API response times
5. **Security Issues**: Review authentication/authorization

---

**Admin Role Version**: 1.0
**Last Updated**: April 5, 2026
**Status**: ✅ Fully Functional

