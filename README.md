# Blogging API

A production-ready Spring Boot REST API for a modern blogging platform. This project provides a clean backend foundation for managing users, posts, categories, tags, and comments while supporting secure authentication, validation, and interactive API documentation.

## Overview

The Blogging API is designed as a full-featured backend service for content-driven applications. It offers a structured and extensible architecture for building blog experiences with minimal setup and fast local development.

## Key Features

- User registration and authentication
- Blog post creation, retrieval, and management
- Category and tag organization
- Comment support for posts
- Pagination and slug-based lookup
- Seeded demo data for immediate testing
- Swagger UI and H2 database console for local development

## Technology Stack

- Java 21
- Spring Boot 3.3.5
- Spring Data JPA
- Spring Validation
- H2 Database
- Springdoc OpenAPI
- Maven

## Prerequisites

Before running the application, ensure the following are installed:

- Java 21 or newer
- Maven 3.9 or newer

## Getting Started

1. Clone the repository.
2. Navigate to the project directory.
3. Start the application:

```bash
mvn spring-boot:run
```

4. Open the following local endpoints:
   - API Base URL: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

## Configuration

The application uses an embedded H2 database stored locally under the data directory.

Default configuration:

- JDBC URL: jdbc:h2:file:./data/blogdb
- Username: sa
- Password: blank

## Sample Data

The application seeds demo content automatically on startup, including:

- A demo author user
- Example categories and tags
- Published sample posts
- A sample comment

Demo login credentials:

- Email: demo@example.com
- Password: password

## Example Requests

### Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Asha Rao","email":"asha@example.com","password":"secret123","bio":"Writer"}'
```

### Authenticate a User

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@example.com","password":"password"}'
```

### Retrieve Published Posts

```bash
curl "http://localhost:8080/api/posts?page=0&size=10&status=PUBLISHED"
```

## Running Tests

Run the test suite with:

```bash
mvn test
```

## License

This project is provided for educational and development purposes.

