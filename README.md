# User Service
REST microservice for user management and authentication

> **Note:** This service is designed to run within the [Orchestra ecosystem](https://github.com/a-partala/orchestra).<br><br>
> Please follow the **[Quick Start Guide](https://github.com/a-partala/orchestra#quick-start)** to spin up the system.

## Technologies
Java 21, Spring Boot (Web, Security, Data JPA), Flyway, PostgreSQL, Docker, JUnit, Mockito, Maven, Lombok

## Functionality
- User registration
- JWT authentication
- Role-based authorization
- Atomic email verification

## Package structure
```
net.partala.user
├── auth/
│   ├── email/
│   └── jwt/
├── config/
├── dto/
│   ├── request/
│   └── response/
├── exception/
└── user/
```

## API endpoints
```
POST /auth/register                 - create new user
POST /auth/login                    - authenticate and get JWT
POST /email/request-verification - temporary, generate verification token
POST /email/verify?token=TOKEN - verify email with token
GET /users/{id}                     - get user by id
POST /users/{id}/promote            - promote user to ADMIN
```

## Request Examples
```
	POST /auth/register
	{
		"username": "alexey",
		"password": "12345678"
	}
	
	POST /auth/login
	{
		"username": "alexey",
		"password": "12345678"
	}
	
	POST /email/request-verification
	"alexey@gmail.com"
```
