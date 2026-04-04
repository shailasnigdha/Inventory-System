# PostgreSQL Setup for Local Development

## Prerequisites
- PostgreSQL 16+ installed locally
- Running on localhost:5432

## Option A: Windows Installation

### 1. Download and Install PostgreSQL
- Download from: https://www.postgresql.org/download/windows/
- During installation, set the password for the `postgres` user to `postgres`
- Keep the default port as 5432
- Start the PostgreSQL service

### 2. Verify Installation
```powershell
# Test connection using psql
psql -U postgres -d postgres
```

When prompted, enter password: `postgres`

### 3. Create Database
```sql
-- In psql shell:
CREATE DATABASE inventorydb;
```

### 4. Run the Application
```powershell
cd D:\inventory
.\mvnw.cmd spring-boot:run
```

---

## Option B: Docker Desktop (Alternative)

### 1. Install Docker Desktop
- Download from: https://www.docker.com/products/docker-desktop
- Install and restart your computer
- Ensure Docker daemon is running

### 2. Start the Application Stack
```powershell
cd D:\inventory

# Copy environment file from template
Copy-Item .env.example .env

# Start PostgreSQL and the application
docker compose up --build
```

The application will be available at: http://localhost:8084

---

## Option C: PostgreSQL in WSL2 (Windows Subsystem for Linux)

If you prefer using WSL2:

```bash
# In WSL2 terminal:
sudo apt update
sudo apt install postgresql postgresql-contrib

# Start PostgreSQL service
sudo service postgresql start

# Create database
sudo -u postgres psql -c "CREATE DATABASE inventorydb;"

# Set postgres password (if not already set)
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'S19151441s';"
```

---

## Troubleshooting

### Error: `password authentication failed for user "postgres"`
- Check that PostgreSQL is running
- Verify the password is correct (should be "postgres" by default)
- On Windows, check Services for PostgreSQL service status

### Error: `FATAL: database "inventorydb" does not exist`
- Connect to PostgreSQL as postgres user
- Run: `CREATE DATABASE inventorydb;`

### How to Connect Directly
```powershell
# Using psql
psql -h localhost -U postgres -d inventorydb

# Password: S19151441s
```

---

## Configuration Files

- **application.properties**: Main configuration with default values
- **.env**: Docker environment variables (create from .env.example)
- **docker-compose.yml**: Docker services definition

Default credentials for local development:
- **Username**: postgres
- **Password**: S19151441s
- **Database**: inventorydb
- **Port**: 5432

