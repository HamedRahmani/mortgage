# Mortgage API

A RESTful API service for mortgage feasibility checks and interest rate management built with Spring Boot.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
  - [Interest Rates](#interest-rates)
  - [Mortgage Check](#mortgage-check)
- [Configuration](#configuration)
- [Testing](#testing)
- [Project Structure](#project-structure)

## Overview

The Mortgage API provides endpoints to retrieve current mortgage interest rates and evaluate mortgage feasibility based on income, loan amount, and home value. The service calculates monthly payments using standard amortization formulas and validates loan requests against configurable business rules.

## Features

- **Interest Rate Management**: Retrieve current mortgage interest rates for various maturity periods
- **Mortgage Feasibility Check**: Validate if a mortgage is feasible based on:
  - Income-based loan limit (4x annual income)
  - Home value constraint (loan cannot exceed home value)
- **Monthly Payment Calculation**: Accurate amortization-based monthly payment calculation
- **Interactive API Documentation**: Swagger UI for API exploration and testing
- **In-Memory Database**: H2 database for development and testing

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming Language |
| Spring Boot | 3.4.2 | Application Framework |
| Spring Data JPA | 3.4.x | Data Persistence |
| H2 Database | 2.x | In-Memory Database |
| SpringDoc OpenAPI | 2.8.4 | API Documentation |
| Maven | 3.9.x | Build Tool |
| JUnit 5 | 5.x | Testing Framework |
| Mockito | 5.x | Mocking Framework |

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.9+** (or use the included Maven Wrapper)

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd mortgage
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

### Running the Application

Start the application using Maven:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the interactive API documentation:

| Resource | URL |
|----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI Spec | http://localhost:8080/api-docs |
| H2 Console | http://localhost:8080/h2-console |

### Interest Rates

#### Get All Interest Rates

Retrieves a list of all currently valid mortgage interest rates.

```http
GET /api/interest-rates
```

**Response:**
```json
[
  {
    "maturityPeriod": 5,
    "interestRate": 4.00,
    "validFrom": "2026-01-01"
  },
  {
    "maturityPeriod": 10,
    "interestRate": 4.25,
    "validFrom": "2026-01-01"
  }
]
```

### Mortgage Check

#### Check Mortgage Feasibility

Evaluates whether a mortgage is feasible and calculates monthly payments.

```http
POST /api/mortgage-check
Content-Type: application/json
```

**Request Body:**
```json
{
  "income": 100000,
  "loanAmount": 300000,
  "homeValue": 400000,
  "maturityPeriod": 10
}
```

**Response (Feasible):**
```json
{
  "feasible": true,
  "monthlyCosts": 3057.35
}
```

**Response (Not Feasible):**
```json
{
  "feasible": false,
  "monthlyCosts": 0
}
```

#### Business Rules

A mortgage is considered **feasible** when:
1. Loan amount ≤ 4 × Annual Income
2. Loan amount ≤ Home Value

## Configuration

The application can be configured via `application.yaml`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8080 | Server port |
| `spring.h2.console.enabled` | true | Enable H2 console |
| `spring.jpa.show-sql` | true | Log SQL statements |

### Database Configuration

The application uses an in-memory H2 database by default:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:mortgagedb
    username: sa
    password:
```

**H2 Console Access:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:mortgagedb`
- Username: `sa`
- Password: *(leave empty)*

### Database Schema

The interest rates table uses validity periods for production-ready historical tracking:

```sql
CREATE TABLE interest_rates (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    maturity_period INT NOT NULL,
    interest_rate   DECIMAL(5,2) NOT NULL,
    valid_from      DATE NOT NULL,
    valid_to        DATE          -- NULL means currently active
);
```

This design allows:
- **Historical tracking**: Keep records of past interest rates
- **Audit compliance**: Track when rates changed
- **Future scheduling**: Pre-configure upcoming rate changes

## Testing

Run all tests:

```bash
./mvnw test
```

Run specific test class:

```bash
./mvnw test -Dtest=MortgageCheckServiceTest
```

### Test Coverage

The project includes comprehensive tests for:

| Layer | Test Class | Coverage |
|-------|------------|----------|
| Controller | `InterestRateControllerTest` | HTTP endpoints, request/response validation |
| Controller | `MortgageCheckControllerTest` | HTTP endpoints, input validation |
| Service | `InterestRateServiceTest` | Business logic, entity mapping |
| Service | `MortgageCheckServiceTest` | Feasibility rules, payment calculations |
| Repository | `InterestRateRepositoryTest` | Data persistence operations |

## Project Structure

```
mortgage/
├── src/
│   ├── main/
│   │   ├── java/com/ing/mortgage/
│   │   │   ├── Application.java
│   │   │   ├── config/                    # Configuration classes
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/                # REST controllers (API layer)
│   │   │   │   ├── InterestRateController.java
│   │   │   │   └── MortgageCheckController.java
│   │   │   ├── dto/                       # Data Transfer Objects
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── InterestRateResponse.java
│   │   │   │   ├── MortgageCheckRequest.java
│   │   │   │   └── MortgageCheckResponse.java
│   │   │   ├── exception/                 # Custom exceptions & handlers
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── InterestRateNotFoundException.java
│   │   │   ├── repository/                # Data access layer
│   │   │   │   ├── InterestRateRepository.java
│   │   │   │   └── model/                 # JPA entities
│   │   │   │       └── InterestRateEntity.java
│   │   │   └── service/                   # Business logic layer
│   │   │       ├── InterestRateService.java
│   │   │       └── MortgageCheckService.java
│   │   └── resources/
│   │       ├── application.yaml           # Application configuration
│   │       └── data.sql                   # Initial data
│   └── test/
│       ├── java/com/ing/mortgage/
│       │   ├── controller/                # Controller tests (@WebMvcTest)
│       │   ├── service/                   # Service tests (unit tests)
│       │   └── repository/                # Repository tests (@DataJpaTest)
│       └── resources/
│           └── application.yaml           # Test configuration
├── pom.xml
├── mvnw
└── README.md
```

### Architecture

The project follows a **layered architecture** pattern:

```
┌─────────────────────────────────────────────────────────────┐
│                      Controller Layer                        │
│              (REST endpoints, request validation)            │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                       Service Layer                          │
│               (Business logic, calculations)                 │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                      Repository Layer                        │
│                  (Data access, persistence)                  │
└─────────────────────────────────────────────────────────────┘
```

## License

This project is proprietary software. All rights reserved.

---

**© 2026 ING. All rights reserved.**


