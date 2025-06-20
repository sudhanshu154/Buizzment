# Buizzment

A Spring Boot application for business management, using MongoDB and JWT-based authentication.

## Features
- RESTful API with Spring Boot
- MongoDB integration
- JWT authentication and Spring Security
- Modular architecture (Controller, Service, Repository, DTO, Model)

## Requirements
- Java 17+
- Maven 3.6+
- MongoDB (local or remote)

## Setup
1. **Clone the repository:**
   ```bash
   git clone <repo-url>
   cd buizzment
   ```
2. **Configure MongoDB:**
   - Set your MongoDB URI in `src/main/resources/application.properties` or `application-dev.properties`.

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   or
   ```bash
   java -jar target/buizzment-0.0.1-SNAPSHOT.jar
   ```

## Project Structure
```
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
```

## Useful Commands
- Run tests: `mvn test`
- Package: `mvn package`

## License
MIT 