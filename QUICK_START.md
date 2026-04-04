# ⚡ Quick Start - 5 Minutes to Running Dashboard

## 🎯 Fastest Way to Get Started

### Step 1: Ensure PostgreSQL is Running (2 min)

**Option A: Using Docker Compose (Easiest)**
```powershell
cd D:\inventory
docker compose up --build
```

**Option B: Local PostgreSQL**
- PostgreSQL must be running on localhost:5432
- Default credentials: postgres / postgres
- Database: inventorydb

---

### Step 2: Run the Application (2 min)

**Option A: Command Line**
```powershell
cd D:\inventory
.\mvnw.cmd spring-boot:run
```

**Option B: Docker Compose** (already running from Step 1)
- App will start automatically
- Access: http://localhost:8084

**Wait for:**
```
Tomcat initialized with port 8084
Root WebApplicationContext: initialization completed
```

---

### Step 3: Access the Dashboard (1 min)

#### 3a. Create a Buyer Account
```
1. Go to: http://localhost:8084/login
2. Click: "Register"
3. Fill in:
   - Username: testbuyer
   - Email: buyer@test.com
   - Password: password123
   - Role: BUYER
4. Click: "Register"
```

#### 3b. Login
```
1. Username: testbuyer
2. Password: password123
3. Click: "Login"
```

#### 3c. Access Dashboard
```
1. Click: "Buyer Dashboard" or
2. Go to: http://localhost:8084/dashboard/buyer
```

---

## 🎨 Dashboard Features - Try Them

### 1️⃣ Browse Products
- See all available products in a grid
- Check prices and stock levels

### 2️⃣ Place an Order
```
1. Select a product from dropdown
2. Enter quantity (e.g., 1)
3. Click "Place Order"
4. See success message
```

### 3️⃣ Track Your Orders
- View your order history in the table below
- See order status with color badges
- New order appears automatically

---

## 📋 Troubleshooting

| Problem | Solution |
|---------|----------|
| "Connection refused" | PostgreSQL not running - See Step 1 |
| Can't login | Check username/password or register first |
| Dashboard blank | Refresh page (F5) |
| Products not loading | Check server logs, refresh page |
| Order won't submit | Ensure BUYER role, check console (F12) |

---

## 📚 Need More Details?

- 📖 **Full Guide:** [BUYER_DASHBOARD_GUIDE.md](./BUYER_DASHBOARD_GUIDE.md)
- 🔧 **Setup Help:** [DATABASE_SETUP.md](./DATABASE_SETUP.md)
- 📚 **All Docs:** [DOCUMENTATION_INDEX.md](./DOCUMENTATION_INDEX.md)
- 🌐 **API Ref:** [API_BUYER_REFERENCE.md](./API_BUYER_REFERENCE.md)

---

## 🎯 Common Tasks

### Place an Order Programmatically
```bash
curl -X POST http://localhost:8084/orders \
  -H "Content-Type: application/json" \
  -d '{"buyerId":1,"productId":2,"quantity":1}' \
  --cookie "JSESSIONID=your_session"
```

### Get Your Order History
```bash
curl http://localhost:8084/orders/buyer/1 \
  --cookie "JSESSIONID=your_session"
```

### Get Your User Info
```bash
curl http://localhost:8084/auth/me \
  --cookie "JSESSIONID=your_session"
```

---

## ✅ Success Checklist

- ✅ PostgreSQL running
- ✅ Application started
- ✅ Created buyer account
- ✅ Logged in
- ✅ Accessed dashboard
- ✅ Saw products
- ✅ Placed an order
- ✅ Order appeared in history

**You're all set! 🚀**

---

**Total Time:** ~5 minutes
**Difficulty:** Easy ⭐
**Help:** See [DOCUMENTATION_INDEX.md](./DOCUMENTATION_INDEX.md)

