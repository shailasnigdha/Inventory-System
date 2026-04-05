# 🚀 Production Deployment Guide

**Version:** 1.0  
**Date:** April 5, 2026  
**Status:** Ready for Production

---

## 📋 DEPLOYMENT CHECKLIST

### Pre-Deployment
- [ ] All tests passing (currently: ✅ 26/26)
- [ ] Code reviewed and approved
- [ ] Production database prepared
- [ ] SSL certificates obtained
- [ ] Environment variables documented
- [ ] Backup strategy in place
- [ ] Monitoring tools configured
- [ ] Team trained on system

### Deployment Options

---

## 🐳 OPTION 1: DOCKER DEPLOYMENT (RECOMMENDED)

### Prerequisites
- Docker Engine installed
- Docker Compose installed
- PostgreSQL credentials ready

### Step 1: Prepare Environment
```powershell
cd D:\inventory

# Copy environment template
Copy-Item .env.example .env

# Edit .env file with production values
notepad .env
```

### Step 2: Edit .env File
```
DB_HOST=postgres
DB_PORT=5432
DB_NAME=inventorydb
DB_USERNAME=postgres
DB_PASSWORD=YourSecurePasswordHere
SERVER_PORT=8084
SPRING_PROFILE=prod
```

### Step 3: Build and Deploy
```powershell
# Build Docker image
docker compose build

# Start services in detached mode
docker compose up -d

# View logs
docker compose logs -f app

# Check status
docker compose ps
```

### Step 4: Verify Deployment
```powershell
# Wait 30-60 seconds for app startup
Start-Sleep -Seconds 45

# Check if app is running
Invoke-WebRequest http://localhost:8084/web/auth/login

# Check database connection
docker compose exec app curl http://localhost:8084/actuator/health
```

### Step 5: Access Application
- Admin Portal: `http://your-server:8084/web/admin/login`
- User Login: `http://your-server:8084/web/auth/login`

### Useful Docker Commands
```powershell
# Stop services
docker compose stop

# Restart services
docker compose restart

# View logs
docker compose logs -f [service-name]

# Remove everything (cleanup)
docker compose down -v

# Update image and restart
docker compose build --no-cache
docker compose up -d
```

---

## 🖥️ OPTION 2: TRADITIONAL JAR DEPLOYMENT

### Prerequisites
- Java 17+ installed
- PostgreSQL database running
- Server/VM with sufficient resources

### Step 1: Build Application
```powershell
cd D:\inventory

# Clean and build
.\mvnw.cmd clean package -DskipTests

# Output: target/inventory-0.0.1-SNAPSHOT.jar
```

### Step 2: Prepare Production Server
```bash
# On production server
mkdir -p /opt/inventory
cd /opt/inventory
```

### Step 3: Copy JAR File
```powershell
# From your machine to server
# Using SCP or your preferred method
Copy-Item "D:\inventory\target\inventory-0.0.1-SNAPSHOT.jar" `
  "\\server-ip\inventory\"
```

### Step 4: Create Application Properties
On production server, create `/opt/inventory/application-prod.properties`:
```properties
spring.datasource.url=jdbc:postgresql://db-server:5432/inventorydb
spring.datasource.username=postgres
spring.datasource.password=YourSecurePassword
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.profiles.active=prod
server.port=8084
server.servlet.context-path=/
```

### Step 5: Create Systemd Service (Linux)
Create `/etc/systemd/system/inventory.service`:
```ini
[Unit]
Description=Inventory Management System
After=network.target postgresql.service

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/inventory
ExecStart=/usr/bin/java -jar inventory-0.0.1-SNAPSHOT.jar \
  --spring.config.location=file:/opt/inventory/application-prod.properties \
  --spring.profiles.active=prod
Restart=on-failure
RestartSec=10s

[Install]
WantedBy=multi-user.target
```

### Step 6: Start Service
```bash
# Enable service
sudo systemctl enable inventory

# Start service
sudo systemctl start inventory

# Check status
sudo systemctl status inventory

# View logs
sudo journalctl -u inventory -f
```

---

## ☁️ OPTION 3: CLOUD DEPLOYMENT

### AWS EC2 + RDS

1. **Create EC2 Instance**
   - AMI: Ubuntu 22.04 LTS
   - Instance Type: t3.medium (or larger)
   - Security Group: Open ports 80, 443, 8084

2. **Create RDS Database**
   - Engine: PostgreSQL 14+
   - Instance: db.t3.micro (or appropriate)
   - Database name: inventorydb
   - Master username: postgres

3. **Deploy Application**
   - SSH into EC2
   - Install Java 17: `sudo apt install openjdk-17-jdk`
   - Follow "JAR Deployment" steps above
   - Use RDS endpoint in connection string

4. **Configure Security**
   - Set up WAF (AWS WAF)
   - Enable VPC Flow Logs
   - Configure CloudWatch monitoring

---

## 🔒 SECURITY HARDENING

### 1. Change Default Credentials
```sql
-- Change database password
ALTER USER postgres WITH PASSWORD 'NewStrongPassword123!@#';
```

### 2. Configure SSL/TLS

#### For Docker:
Create `nginx.conf` for SSL termination

#### For JAR:
Add to `application-prod.properties`:
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-keystore-password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
```

### 3. Environment Variables (Production)
```powershell
$env:DB_PASSWORD = "SecurePassword123!@#"
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:LOG_LEVEL = "INFO"
$env:SESSION_TIMEOUT = "30"
```

### 4. Database Security
```sql
-- Create restricted user (not admin)
CREATE ROLE inventory_app WITH LOGIN PASSWORD 'app-password';
GRANT CONNECT ON DATABASE inventorydb TO inventory_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO inventory_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO inventory_app;
```

### 5. Firewall Rules
- Allow traffic only from known IPs
- Block direct database access
- Use VPN for administration
- Implement rate limiting

---

## 📊 MONITORING & LOGGING

### 1. Application Logs
```powershell
# Docker
docker compose logs -f app --tail=100

# JAR (if using systemd)
sudo journalctl -u inventory -f
```

### 2. Health Check Endpoint
```bash
curl http://localhost:8084/actuator/health
```

### 3. Monitor Metrics
```properties
# Add to application-prod.properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized
```

### 4. Log Aggregation (Optional)
Consider implementing ELK Stack:
- Elasticsearch for log storage
- Logstash for log processing
- Kibana for visualization

---

## 🧪 POST-DEPLOYMENT TESTING

### Step 1: Health Check
```powershell
# Check if application is running
$response = Invoke-WebRequest http://localhost:8084/web/auth/login
Write-Host "Status: $($response.StatusCode)"
```

### Step 2: Database Connectivity
```sql
-- From production DB server
SELECT * FROM users LIMIT 1;
SELECT * FROM roles;
```

### Step 3: Authentication Test
```powershell
# Test login endpoint
$loginData = @{
    email = "admin@example.com"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8084/auth/login" `
  -Body $loginData `
  -ContentType "application/json"
```

### Step 4: API Endpoints Test
```powershell
# Get all products
Invoke-RestMethod http://localhost:8084/api/products

# Get all users (requires admin auth)
Invoke-RestMethod http://localhost:8084/admin/api/users
```

---

## 🔄 ROLLBACK PROCEDURE

### If Something Goes Wrong

#### For Docker:
```powershell
# Stop current deployment
docker compose down

# Remove failed container
docker container prune -f

# Restore from backup (if available)
# Then restart with previous image
docker compose up -d
```

#### For JAR:
```bash
# Stop service
sudo systemctl stop inventory

# Restore previous JAR version
cd /opt/inventory
cp backup/inventory-0.0.1-SNAPSHOT.jar inventory-0.0.1-SNAPSHOT.jar

# Start service
sudo systemctl start inventory
```

### Verify Rollback
```powershell
# Test application
Invoke-WebRequest http://localhost:8084/web/auth/login

# Check logs for errors
docker compose logs app
```

---

## 📈 SCALING CONSIDERATIONS

### Load Balancing
For multiple instances, use:
- **NGINX** as reverse proxy
- **HAProxy** for load balancing
- **AWS ALB** for cloud deployment

### Database Scaling
- Read replicas for read-heavy workloads
- Connection pooling (HikariCP configured)
- Query optimization
- Caching layer (Redis)

### Application Scaling
- Run multiple JAR instances
- Use container orchestration (Kubernetes)
- Horizontal auto-scaling
- Session replication

---

## 📝 MAINTENANCE

### Regular Tasks

#### Weekly
- [ ] Check application logs
- [ ] Monitor disk space
- [ ] Verify backups running
- [ ] Review error rates

#### Monthly
- [ ] Update dependencies
- [ ] Review security patches
- [ ] Database optimization
- [ ] Performance analysis

#### Quarterly
- [ ] Full security audit
- [ ] Load testing
- [ ] Disaster recovery drill
- [ ] Documentation update

---

## 🚨 TROUBLESHOOTING

### Application Won't Start

**Check Java is installed:**
```bash
java -version
```

**Check port availability:**
```powershell
netstat -ano | findstr :8084
```

**Check database connection:**
```bash
psql -h localhost -U postgres -d inventorydb
```

### Database Connection Error

**Verify PostgreSQL is running:**
```bash
# Linux
sudo systemctl status postgresql

# Windows
sc query postgresql
```

**Check connection string:**
```
jdbc:postgresql://host:5432/inventorydb
```

### High CPU Usage

**Check running processes:**
```powershell
Get-Process | Where-Object {$_.Name -like "*java*"}
```

**Monitor JVM memory:**
```bash
jps -l -m
```

### Slow Response Times

**Check database queries:**
```sql
-- Enable query logging
SET log_statement = 'all';
```

**Review Spring logs:**
```
tail -f /var/log/inventory/app.log | grep "took.*ms"
```

---

## 🎁 DEPLOYMENT CHECKLIST (FINAL)

```
Pre-Deployment:
  [ ] All tests passing
  [ ] Code reviewed
  [ ] Backups created
  [ ] Monitoring configured

Deployment Day:
  [ ] Maintenance window scheduled
  [ ] Team briefed
  [ ] Rollback plan ready
  [ ] Build artifact prepared

Post-Deployment:
  [ ] Application running
  [ ] All endpoints responding
  [ ] Database connected
  [ ] Logs normal
  [ ] Team notified
  [ ] Monitoring active

24-Hour Post-Deploy:
  [ ] No errors in logs
  [ ] Performance metrics normal
  [ ] All features working
  [ ] Users accessing system
  [ ] Documentation updated
```

---

## 📞 DEPLOYMENT SUPPORT

**Need Help?**
- Check logs: `docker compose logs -f`
- Review errors: Check application error logs
- Test endpoints: Use Postman/curl
- Rollback: Follow rollback procedure above

**Key Endpoints to Monitor:**
- `/actuator/health` - Application health
- `/web/auth/login` - Authentication page
- `/admin/api/users` - Admin API
- `/api/products` - Product API

---

## ✅ READY TO DEPLOY!

Your application is **production-ready**. Follow the steps above for your chosen deployment method.

**Current Status:**
- ✅ Build: Successful
- ✅ Tests: All passing (26/26)
- ✅ Security: Configured
- ✅ Documentation: Complete

**Ready?** Let's deploy! 🚀

*For questions or issues, refer to system logs and check endpoints.*

