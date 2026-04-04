# Seller Dashboard Fix - Testing Guide

## Pre-Testing Setup

### 1. Rebuild the Project
```bash
cd D:\inventory
.\mvnw clean compile -q
```

**Expected Output:** No errors (warnings about Unsafe are safe to ignore)

### 2. Start the Application
```bash
.\mvnw spring-boot:run
```

**Expected:** Application starts on `http://localhost:8084`

### 3. Prepare Test Data
- If needed, create test accounts:
  - Seller account: username=`seller1`, password=`pass123`
  - Buyer account: username=`buyer1`, password=`pass123`

---

## Test Case 1: Product Loading Performance

**Objective:** Verify products load quickly without N+1 queries

### Steps:
1. Open browser Developer Tools (F12)
2. Go to **Network** tab
3. Navigate to Seller Dashboard and login
4. Watch the **My Products** tab load

### Expected Results:
✅ Products list appears **within 1 second**  
✅ **Network** tab shows 1 request to `/products/seller/{id}`  
✅ Response time: **<500ms**  
✅ No "Loading your products..." spinner visible  
✅ Table displays all products with: Name, Description, Price, Stock, Value

### If FAILING:
- ❌ Products take 5+ seconds to load → Check if eager loading is applied
- ❌ Multiple requests shown in Network tab → N+1 problem still exists
- ❌ Products show partial data (no names/prices) → Lazy loading issue

---

## Test Case 2: Order Notifications Loading

**Objective:** Verify order notifications work without lazy loading errors

### Steps:
1. With Developer Tools open, go to **Console** tab
2. Click **Order Notifications** tab
3. Watch for any errors in console

### Expected Results:
✅ Orders list appears **within 1 second**  
✅ **Console** shows no errors  
✅ **Network** tab shows 1 request to `/orders/seller/{id}`  
✅ Response time: **<500ms**  
✅ Each order shows:
  - Product name
  - Order ID
  - Quantity
  - Price per unit
  - Buyer username
  - Order date/time
  - Status (Pending/Delivered)

### If FAILING:
- ❌ "Failed to load orders" message → Lazy loading errors in backend
- ❌ Console shows errors like "Cannot read property 'username' of null" → Object not loaded
- ❌ Empty fields in order display → Relationships not eager loaded

---

## Test Case 3: Product Creation

**Objective:** Verify products can be created and appear immediately

### Steps:
1. Go to **My Products** tab
2. Fill in the form:
   - Name: `Test Product`
   - Price: `99.99`
   - Quantity: `10`
   - Description: `This is a test product`
3. Click **Create Product**

### Expected Results:
✅ Success message appears: "✓ Product created successfully!"  
✅ Form clears automatically  
✅ Product appears in catalog table immediately  
✅ Stats update (Total Products +1, Total Stock +10, Total Value +999.90)  
✅ Network tab shows POST to `/products` followed by GET to `/products/seller/{id}`

### If FAILING:
- ❌ Error message appears → Check product validation
- ❌ Product doesn't appear after creation → Cache issue or reload problem
- ❌ Stats don't update → Frontend calculation error

---

## Test Case 4: Order Confirmation

**Objective:** Verify orders can be marked as delivered

### Prerequisites:
- An existing order with status "Pending"

### Steps:
1. Go to **Order Notifications** tab
2. Find an order with "Pending" status
3. Click **✓ Confirm & Deliver** button
4. Confirm the dialog

### Expected Results:
✅ Success message: "✓ Order marked as delivered!"  
✅ Order disappears from list or shows "Delivered" status  
✅ Network tab shows PUT request to `/orders/{orderId}/confirm`  
✅ No errors in Console

### If FAILING:
- ❌ Button doesn't respond → JavaScript event handler issue
- ❌ Error message appears → Backend validation issue
- ❌ Order status doesn't change → Update not persisting

---

## Performance Benchmarking

### Recommended: Use Browser DevTools

**Open Developer Tools:**
1. Press F12
2. Go to **Network** tab
3. Reload the dashboard

**Measure Response Times:**

| Endpoint | Expected Time | Actual Time | Status |
|----------|---|---|---|
| `/products/seller/{id}` | <500ms | _____ | _____ |
| `/orders/seller/{id}` | <500ms | _____ | _____ |
| `/auth/me` | <200ms | _____ | _____ |

### Advanced: Use Database Query Log

**Enable SQL logging in `application.properties`:**
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

**Expected SQL for products:**
```sql
-- Should see 1 query with JOIN
SELECT p.*, u.* FROM products p 
LEFT JOIN users u ON p.seller_id = u.id 
WHERE p.seller_id = ?
```

**NOT expected (N+1 problem):**
```sql
-- Should NOT see 101 queries like this
SELECT * FROM products WHERE seller_id = ?
SELECT * FROM users WHERE id = ?  -- repeated 100 times
```

---

## Browser Compatibility Testing

Test on multiple browsers to ensure consistency:

| Browser | Version | Status |
|---------|---------|--------|
| Chrome | Latest | _____ |
| Firefox | Latest | _____ |
| Edge | Latest | _____ |
| Safari | Latest | _____ |

---

## Regression Testing Checklist

Verify existing functionality still works:

- [ ] User login/logout works
- [ ] Role-based access control works
- [ ] Buyer can see products
- [ ] Buyer can place orders
- [ ] Admin can view all data
- [ ] Product editing works (if implemented)
- [ ] Product deletion works (if implemented)
- [ ] Order cancellation works (if implemented)

---

## Load Testing (Optional)

For large datasets:

### Test with Many Products
```javascript
// In browser console, test with 1000+ products
// This tests pagination and performance at scale
fetch('/products/seller/1')
  .then(r => r.json())
  .then(d => console.log(`Loaded ${d.length} products`))
```

### Expected:
✅ Should still complete in <1 second with 1000 products  
✅ UI remains responsive

---

## Issue Resolution Guide

### Issue: Slow Product Loading (5+ seconds)

**Diagnosis:**
1. Check Network tab - multiple requests? → N+1 problem
2. Check ProductRepository - has `LEFT JOIN FETCH`? 
3. Run database query with EXPLAIN - using index?

**Resolution:**
- [ ] Verify ProductRepository has eager loading
- [ ] Verify schema.sql indexes are created
- [ ] Rebuild and restart application

### Issue: Order Notifications Fail

**Diagnosis:**
1. Check Console tab - JavaScript errors?
2. Check Network tab - 500 error from backend?
3. Check backend logs - lazy loading exceptions?

**Resolution:**
- [ ] Verify OrderRepository has complete eager loading
- [ ] Verify all relationships (buyer, product, seller) are fetched
- [ ] Check Order entity - all fields accessible?

### Issue: Product Not Saving

**Diagnosis:**
1. Check Network tab - POST successful but no refresh?
2. Check Console - JavaScript errors?
3. Check backend logs - validation errors?

**Resolution:**
- [ ] Verify form validation on frontend
- [ ] Check ProductService.createProduct() implementation
- [ ] Verify database connection

### Issue: JavaScript Errors in Console

**Expected:** No errors (warning about sun.misc.Unsafe is acceptable)

**If errors found:**
- [ ] Check seller-dashboard.html for syntax errors
- [ ] Verify all JavaScript functions are properly closed
- [ ] Clear browser cache and reload

---

## Sign-Off Checklist

Before considering the fix complete:

- [ ] **Product Loading:** Loads in <500ms
- [ ] **Order Notifications:** Work correctly without errors
- [ ] **Database:** Indexes created successfully
- [ ] **Code:** No compilation errors
- [ ] **Browser Console:** No JavaScript errors
- [ ] **Network Tab:** Correct number of requests
- [ ] **Functionality:** All features work as expected
- [ ] **Performance:** Noticeable improvement from before fix

---

## Rollback Plan (If Issues Found)

If critical issues occur after deployment:

1. **Immediate:** Revert changes to repository classes
2. **Fallback Query:** Use simpler queries without eager loading
3. **Temporary:** Disable new indexes if causing issues
4. **Investigate:** Identify root cause before retrying

### Commands:
```bash
# Recompile with previous version
git revert <commit-hash>
.\mvnw clean compile
.\mvnw spring-boot:run
```

---

**Testing Date:** _____________  
**Tester Name:** _____________  
**Status:** ☐ PASS ☐ FAIL  

**Notes:**
__________________________________________________________
__________________________________________________________

---

**Document Version:** 1.0  
**Date Created:** April 4, 2026

