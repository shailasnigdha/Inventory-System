@echo off
REM Quick startup script for Inventory Management System

echo.
echo ============================================
echo  Inventory Management System - Local Setup
echo ============================================
echo.

REM Check if PostgreSQL is running
echo Checking PostgreSQL connection...
sqlcmd -S localhost -U postgres -P postgres -d inventorydb -Q "SELECT 1" > nul 2>&1

if errorlevel 1 (
    echo.
    echo ERROR: Cannot connect to PostgreSQL!
    echo.
    echo Please ensure:
    echo   1. PostgreSQL is installed and running
    echo   2. Database 'inventorydb' exists
    echo   3. Default credentials: postgres / postgres
    echo.
    echo For setup instructions, see DATABASE_SETUP.md
    echo.
    pause
    exit /b 1
)

echo ✓ PostgreSQL connection successful
echo.
echo Starting Spring Boot application...
echo.

.\mvnw.cmd spring-boot:run

pause

