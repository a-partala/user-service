# User Service
REST microservice for user management and identity provisioning.

> **Note:** This service is designed to run within the [Orchestra](https://github.com/a-partala/orchestra) ecosystem.  
> Please follow the **[Quick Start Guide](https://github.com/a-partala/orchestra#quick-start)** to spin up the system.

## Technologies
Java 21, Spring Boot (Web, Security, Data JPA), Flyway, PostgreSQL, Docker, Maven, Lombok.

## Engineering Highlight: Identity Provider (IdP) & Security

This service acts as the **Single Source of Truth** for the entire ecosystem. It manages the lifecycle of user identities and ensures secure access control.

* **Identity Provisioning**: Instead of sharing a database, this service issues signed **JWT tokens**. This allows other services (like Task Service) to verify users statelessly without direct access to user credentials.
* **Secure Credential Storage**: Implements **BCrypt** hashing for passwords. Sensitive data is never stored or transmitted in plain text.
* **Atomic Verification Logic**: Features a robust email verification system. Using a token-based approach, it ensures that account activation is decoupled from the main registration flow.

## Functionality
- **Centralized Authentication**: Registration and Login with JWT issuance.
- **Security**: BCrypt password encoding and Role-Based Access Control (RBAC).
- **Identity Lifecycle**: Email verification and administrative user promotion.
- **Data Integrity**: Flyway-managed schema migrations for user and role tables.

## Package Structure
```
net.partala.userservice
├── auth/                 # Identity & Auth logic
│   ├── email/              - Email verification flow (Onboarding)
│   └── jwt/                - JWT generation and signing
├── config/               # Security & App configuration
├── dto/                  # Data transfer objects
│   ├── request/            - Request models
│   └── response/           - Response models
├── exception/            # Centralized error handling
└── user/                 # Core User domain (Entity, Service, Repo)
```

## API Endpoints
| Method    | Endpoint                      | Description                   |
|:----------|:------------------------------|:------------------------------|
| **POST**  | `/auth/register`              | Create a new user account     |
| **POST**  | `/auth/login`                 | Authenticate and receive JWT  |
| **POST**  | `/email/request-verification` | Generate a verification token |
| **POST**  | `/email/verify?token=...`     | Confirm email ownership       |
| **GET**   | `/users/{id}`                 | Get user data                 |
| **POST**  | `/users/{id}/promote`         | Promote user to ADMIN role    |

## Request Examples

### Registration & Login
```json
POST /auth/register
{
"username": "admin",
"password": "password"
}

POST /auth/login -> Returns JWT Token
```

### Email Verification Flow
```json
// Authorized request: Generate token
POST /email/request-verification
{
"token": "..." // temporary returned in body for testing
}

// Unauthorized request: Confirm via link/token
POST /email/verify?token=EMAIL_VERIFICATION_TOKEN
```
