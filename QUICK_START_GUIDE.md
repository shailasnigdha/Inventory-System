# Quick Start Guide - Inventory Management System

## 🚀 Getting Started in 3 Minutes

### Prerequisites
- Java 17+
- Maven 3.9+
- Docker & Docker Compose (optional)
- PostgreSQL 15+ (optional)

---

## ⚡ Quick Start Options

### Option 1: Run with Maven (Fastest)
```bash
# Navigate to project directory
cd D:\inventory

# Clean build and install
.\mvnw.cmd clean install -DskipTests

# Run the application
.\mvnw.cmd spring-boot:run
```

**Access at**: http://localhost:8084

---

### Option 2: Run with Docker
```bash
# Build Docker image
docker build -t inventory-app:latest .

# Run container
docker run -d -p 8084:8084 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/inventory_db \
  inventory-app:latest

# Check logs
docker logs -f inventory-app
```

**Access at**: http://localhost:8084

---

### Option 3: Run with Docker Compose (Full Stack)
```bash
# Navigate to project directory
cd D:\inventory

# Start all services (PostgreSQL + App)
docker-compose up --build

# View logs
docker-compose logs -f app
```

**Services Started**:
- App: http://localhost:8084
- PostgreSQL: localhost:5432

---

## 🔑 Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Seller | seller1 | seller123 |
| Buyer | buyer1 | buyer123 |

---

## 📱 API Endpoints

### Authentication
```
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/logout
```

### Products
```
GET    /api/products                    # List all products
GET    /api/products/{id}               # Get specific product
POST   /api/products                    # Create product (SELLER)
PUT    /api/products/{id}               # Update product (SELLER)
DELETE /api/products/{id}               # Delete product (SELLER)
```

### Orders
```
POST   /api/orders                      # Place order (BUYER)
GET    /api/orders/buyer/{id}           # Get buyer orders (BUYER)
GET    /api/orders/seller/{id}          # Get seller orders (SELLER)
PUT    /api/orders/{id}/confirm         # Confirm order (SELLER)
PUT    /api/orders/{id}/deliver         # Mark delivered (SELLER)
DELETE /api/orders/{id}                 # Cancel order (BUYER)
```

---

## 🗃️ Database Configuration

### Local Development (H2)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

**H2 Console**: http://localhost:8084/h2-console

### Production (PostgreSQL)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## 🔄 Environment Profiles

### Development Profile
```bash
# Uses application.properties
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Docker Profile
```bash
# Uses application-docker.properties
docker run -e SPRING_PROFILES_ACTIVE=docker s19151441s/inventory-app:latest
```

### Test Profile
```bash
# Uses application-test.properties with H2 database
mvn test
```

---

## 📦 Build & Package

### Build JAR
```bash
.\mvnw.cmd clean package
```

**Output**: `target/inventory-0.0.1-SNAPSHOT.jar`

### Run JAR
```bash
java -jar target/inventory-0.0.1-SNAPSHOT.jar
```

### Build Docker Image
```bash
docker build -t s19151441s/inventory-app:latest .
```

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthControllerTest
```

### Run with Coverage
```bash
mvn test jacoco:report
```

---

## 📊 Project Structure

```
inventory/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/seproject/inventory/
│   │   │       ├── controller/       # REST endpoints
│   │   │       ├── service/          # Business logic
│   │   │       ├── repository/       # Data access (JPA)
│   │   │       ├── entity/           # JPA entities
│   │   │       ├── dto/              # Data transfer objects
│   │   │       ├── security/         # Security config
│   │   │       └── exception/        # Exception handling
│   │   └── resources/
│   │       ├── static/               # CSS, JS, images
│   │       ├── templates/            # Thymeleaf templates
│   │       └── application*.properties
│   └── test/
│       └── java/
│           └── com/seproject/inventory/
│               ├── controller/       # Controller tests
│               └── service/          # Service tests
├── Dockerfile                        # Container configuration
├── docker-compose.yml               # Multi-container setup
└── pom.xml                          # Maven dependencies
```

---

## 🐛 Troubleshooting

### Port 8084 Already in Use
```bash
# Find process using port 8084
netstat -ano | findstr :8084

# Kill process (if PID is 1234)
taskkill /PID 1234 /F
```

### PostgreSQL Connection Error
```bash
# Start PostgreSQL with Docker Compose
docker-compose up postgres

# Or verify PostgreSQL is running
psql -U postgres
```

### Maven Build Fails
```bash
# Clear Maven cache
rmdir /s %USERPROFILE%\.m2\repository

# Rebuild
mvn clean install -DskipTests
```

### Docker Image Push Fails
```bash
# Ensure lowercase image name
docker tag s19151441s/inventory-app:latest s19151441s/inventory-app:latest

# Login to Docker Hub
docker login

# Push
docker push s19151441s/inventory-app:latest
```

---

## 📝 Key Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven dependencies and build config |
| `Dockerfile` | Docker image build instructions |
| `docker-compose.yml` | Multi-container orchestration |
| `application.properties` | Main configuration |
| `application-docker.properties` | Docker-specific config |
| `application-test.properties` | Test configuration |

---

## 🔗 Useful Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://spring.io/projects/spring-security)
- [Docker Documentation](https://docs.docker.com)
- [PostgreSQL Documentation](https://www.postgresql.org/docs)
- [JPA Documentation](https://hibernate.org/orm)

---

## ✅ Verification Checklist

After running the application, verify:

- [ ] Application starts without errors
- [ ] Can access http://localhost:8084
- [ ] Can login with default credentials
- [ ] Can view dashboard (buyer/seller based on role)
- [ ] Can create products (as seller)
- [ ] Can view products (as buyer)
- [ ] Can place orders (as buyer)
- [ ] Can confirm orders (as seller)

---

## 📞 Support

For issues or questions:
1. Check logs: `docker logs inventory-app` or console output
2. Review error messages in `application.log`
3. Check GitHub Issues: https://github.com/shailasnigdha/Inventory-System/issues
4. Contact: Your support email

---

**Last Updated**: April 5, 2026
**Version**: 1.0.0

