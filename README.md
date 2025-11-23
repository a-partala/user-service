
# User Service
REST API for user management and authentication

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
POST /auth/email-verification-token - temporary, generate verification token
POST /auth/verify-email?token=TOKEN - verify email with token
GET /users/{id}                     - get user by id
POST /users/{id}/promote            - promote user to ADMIN
```
## Request Examples
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
