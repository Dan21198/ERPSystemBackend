# Enterprise Resource Planning System

A comprehensive Spring Boot-based backend application for enterprise resource planning, focusing on employee management, project tracking, contract administration, and financial planning.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database](#database)
- [Docker Support](#docker-support)
- [Project Structure](#project-structure)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Employee Management**: Track employee information, contracts, and work assignments
- **Project Management**: Create and manage projects with financial tracking
- **Assignment Management**: Assign employees to projects with tariff and workload management
- **Contract Management**: Handle employee contracts with expiration tracking
- **Performance Bonuses**: Calculate and manage performance-based bonuses
- **Position Management**: Define project positions and allocate resources
- **User Authentication**: JWT-based authentication and authorization with email verification
- **Financial Planning**: Track total amount spent and allocated per project
- **Automated Status Updates**: Scheduled tasks for project status and contract expiration tracking
- **Comprehensive Validation**: Input validation with detailed error messages
- **CORS Support**: Cross-origin resource sharing configuration
- **Swagger UI**: Interactive API documentation

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security with JWT**
- **Jakarta Persistence API**
- **Lombok**
- **Flyway** - Database migrations
- **PostgreSQL**
- **Docker & Docker Compose**
- **Maven**
- **Swagger/OpenAPI** - API documentation

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12.0+ (or MySQL 8.0+ if modified)
- Docker & Docker Compose (optional, for containerized deployment)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/RPR1BE.git
cd RPR1BE
```

### 2. Configure Environment Variables

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` with your actual configuration:

```dotenv
POSTGRES_DB=your_database_name
POSTGRES_USER=your_database_user
POSTGRES_PASSWORD=your_secure_password
DB_HOST=localhost
DB_PORT=5432
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

### 3. Build the Project

```bash
./mvnw clean package
```

On Windows:
```bash
mvnw.cmd clean package
```

### 4. Database Setup (Manual Setup)

Create a PostgreSQL database:

```sql
CREATE DATABASE rpr1_db;
CREATE USER rpr1_user WITH PASSWORD 'your_password';
ALTER ROLE rpr1_user SET client_encoding TO 'utf8';
ALTER ROLE rpr1_user SET default_transaction_isolation TO 'read committed';
ALTER ROLE rpr1_user SET default_transaction_deferrable TO on;
ALTER ROLE rpr1_user SET timezone TO 'UTC';
GRANT ALL PRIVILEGES ON DATABASE rpr1_db TO rpr1_user;
```

## Configuration

Update `src/main/resources/application.properties`:

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database - PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/rpr1_db
spring.datasource.username=rpr1_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL13Dialect
spring.jpa.properties.hibernate.format_sql=true

# Flyway
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

# JWT Configuration
jwt.secret.key=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
jwt.refresh.expiration=${JWT_REFRESH_EXPIRATION}

# Email Configuration
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# Logging
logging.level.root=INFO
logging.level.osu=DEBUG
```

## Running the Application

### Using Maven

```bash
./mvnw spring-boot:run
```

### Using Java

```bash
java -jar target/RPR1BE-1.0.0.jar
```

### Using Docker Compose

```bash
docker-compose up -d
```

The application will be available at `http://localhost:8080`

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Main Endpoints

#### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - User login
- `POST /auth/logout` - User logout

#### Employees
- `GET /employees` - List all employees
- `POST /employees` - Create new employee
- `GET /employees/{id}` - Get employee details
- `PUT /employees/{id}` - Update employee
- `DELETE /employees/{id}` - Delete employee

#### Projects
- `GET /projects` - List all projects
- `POST /projects` - Create new project
- `GET /projects/{id}` - Get project details
- `PUT /projects/{id}` - Update project
- `DELETE /projects/{id}` - Delete project

#### Assignments
- `GET /assignments` - List all assignments
- `POST /assignments` - Create new assignment
- `GET /assignments/{id}` - Get assignment details
- `PUT /assignments/{id}` - Update assignment
- `DELETE /assignments/{id}` - Delete assignment

#### Contracts
- `GET /contracts` - List all contracts
- `POST /contracts` - Create new contract
- `GET /contracts/{id}` - Get contract details
- `PUT /contracts/{id}` - Update contract

#### Positions
- `GET /positions` - List all positions
- `POST /positions` - Create new position
- `GET /positions/{id}` - Get position details
- `PUT /positions/{id}` - Update position

#### Performance Bonuses
- `GET /bonuses` - List all bonuses
- `POST /bonuses` - Create new bonus
- `GET /bonuses/{id}` - Get bonus details

#### Tariffs
- `GET /tariffs` - List all tariffs
- `POST /tariffs` - Create new tariff
- `GET /tariffs/{id}` - Get tariff details

### Example Request

```bash
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "userDefinedId": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "titleBeforeName": "Mr.",
    "titleAfterName": "PhD",
    "contractStart": "2024-01-01",
    "contractEnd": "2025-12-31",
    "workloadPercentage": 100.0,
    "tariffAmount": 5000.0
  }'
```

## Database

### Migrations

Database migrations are managed using Flyway. Migration files are located in:
- `src/main/resources/db/migration/`

Migrations are automatically executed on application startup.

### Database Schema

Key entities:
- `app_user` - User accounts with authentication
- `employee` - Employee information with contract details
- `project` - Project definitions with financial tracking
- `position` - Project positions and resource allocation
- `assignment` - Employee assignments to positions
- `contract` - Employment contracts
- `tariff` - Salary tariff information
- `performance_bonus` - Performance-based bonuses
- `jwt_blacklist` - Token blacklist for logout

## Docker Support

### Running with Docker Compose

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database (port 5432)
- Spring Boot application (port 8080)

### Building Docker Image

```bash
docker build -t rpr1be:latest .
```

### Running as Container

```bash
docker run -p 8080:8080 --env-file .env rpr1be:latest
```

### Docker Compose Configuration

The `docker-compose.yml` file includes:
- PostgreSQL 13 service
- Spring Boot application service with environment variables
- Volume persistence for database data
- Network configuration for service communication

## Project Structure

```
RPR1BE/
├── src/main/java/osu/
│   ├── Rpr1Application.java           # Main application class
│   ├── assignment/                    # Assignment management
│   │   ├── component/                 # Scheduled tasks
│   │   ├── controller/                # REST endpoints
│   │   ├── mapper/                    # DTO mapping
│   │   ├── model/                     # Entity and DTO classes
│   │   ├── repository/                # Database access
│   │   └── service/                   # Business logic
│   ├── auth/                          # Authentication & JWT
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── config/                        # Spring configuration
│   ├── contract/                      # Contract management
│   ├── employee/                      # Employee management
│   ├── exception/                     # Global exception handling
│   ├── performanceBonus/              # Performance bonus management
│   ├── position/                      # Position management
│   ├── project/                       # Project management
│   ├── tariff/                        # Tariff management
│   └── user/                          # User management
├── src/main/resources/
│   ├── application.properties         # Spring configuration
│   └── db/migration/                  # Flyway SQL migrations
│       ├── V1__.sql
│       ├── V2__.sql
│       ├── V3__Insert_reference_data_with_user6.sql
│       └── V4__.sql
├── src/test/
│   └── java/osu/                      # Unit and integration tests
├── .env.example                       # Environment variables template
├── .gitignore                         # Git ignore rules
├── pom.xml                            # Maven configuration
├── Dockerfile                         # Docker image configuration
├── docker-compose.yml                 # Docker Compose configuration
├── mvnw & mvnw.cmd                    # Maven wrapper
└── README.md                          # This file
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Security

- JWT tokens are used for authentication
- Passwords are hashed before storage
- CORS is configured for secure cross-origin requests
- Input validation is performed on all endpoints
- SQL injection is prevented using parameterized queries (JPA)

### Important Security Notes

- Change JWT_SECRET in production
- Use HTTPS in production
- Keep dependencies updated
- Enable database connection encryption
- Use strong passwords for database users
