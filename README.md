
# User Service
REST microsrvice for user management and authentication

## Technologies
Java 21, Spring Boot (Web, Security, Data JPA), PostgreSQL, Docker, JUnit, Mockito, Maven, Lombok

## Functionality
- User registration
- JWT authentication
- Role-based authorization
- Email verification (temporary stub, no email sending)

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

## Run locally
Prerequisite:
- PostgreSQL running locally
```
mvn clean install
mvn spring-boot:run
```

## Request Examples
```
	POST /auth/register
	{
		"username": "alexey",
		"email": "alexey@gmail.com",
		"password": "12345678"
	}
	
	POST /auth/login
	{
		"username": "alexey",
		"password": "12345678"
	}
```
