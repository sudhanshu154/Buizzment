
Project Structure
First, let's create the basic project structure:
buizzment/
├── src/
│   ├── main/
│   │   ├── java/com/buizzment/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── BuizzmentApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
└── pom.xml




POST /api/auth/signup
Content-Type: application/json

{
"name": "John Doe",
"username": "johndoe",
"email": "john@example.com",
"password": "password123"
}




POST /api/auth/signin
Content-Type: application/json

{
"usernameOrEmail": "johndoe",
"password": "password123"
}



GET /api/test/user
Authorization: Bearer <token-from-signin>



sequenceDiagram
Client->>+Server: GET /api/test/user (with JWT)
Server->>+JwtFilter: Extract/validate token
JwtFilter->>+UserDetailsService: Load user by ID
UserDetailsService->>+Database: Get user + roles
Database-->>-UserDetailsService: User data
UserDetailsService-->>-JwtFilter: UserPrincipal with authorities
JwtFilter->>+SecurityContext: Store authentication
SecurityContext-->>-JwtFilter: Confirm
JwtFilter->>+Controller: Forward request
Controller->>+Security: Check @PreAuthorize
Security-->>-Controller: Authorization result
Controller-->>-Client: Response



{
"name": "Sudhanshu",
"username": "Sudhanshu",
"email": "Sudhanshu@example.com",
"password": "password123",
"roles": "ADMIN"
}