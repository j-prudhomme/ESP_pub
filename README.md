# Room8 Backend Technical Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technical Stack](#technical-stack)
3. [Database Schema](#database-schema)
4. [Application Architecture](#application-architecture)
5. [Security Implementation](#security-implementation)
6. [API Endpoints](#api-endpoints)
7. [Service Layer](#service-layer)
8. [Data Access Layer](#data-access-layer)
9. [Email Service](#email-service)
10. [Configuration](#configuration)
11. [JWT Authentication Flow](#jwt-authentication-flow)
12. [Hotel Reservation Logic](#hotel-reservation-logic)
13. [Error Handling](#error-handling)
14. [Testing Strategy](#testing-strategy)

## Project Overview

Room8 is a hotel reservation system backend built using Spring Boot. The application provides functionality for user authentication, hotel listings, and room reservations. The system allows users to register, login, search for hotels based on location and availability, and manage their profile and booking history.

### Key Features
- User registration and authentication with JWT tokens
- Hotel and room management
- Room availability search based on dates and location
- Secure password reset functionality via email
- Profile management

## Technical Stack

### Technologies
- **Java**: Version 17+ (based on Spring Boot requirements)
- **Spring Boot**: Framework for application development
- **Spring Security**: For authentication and authorization
- **Spring Data JPA**: For database access and ORM
- **Spring Mail**: For email services
- **MySQL**: Primary database
- **JWT (JSON Web Tokens)**: For stateless authentication
- **BCrypt**: For password hashing
- **Lombok**: To reduce boilerplate code

### Dependencies
- `spring-boot-starter-web`: For RESTful API development
- `spring-boot-starter-data-jpa`: For database operations
- `spring-boot-starter-security`: For security implementation
- `spring-boot-starter-mail`: For email services
- `mysql-connector-java`: For MySQL connection
- `jjwt`: JWT library for token generation and validation
- `lombok`: For reducing boilerplate code
- `spring-boot-devtools`: For development-time features

## Database Schema

### Entities

#### User
- `id`: Long (Primary Key)
- `fullName`: String (Not Null)
- `email`: String (Not Null, Unique)
- `password`: String (Not Null) - BCrypt hashed
- `resetToken`: String (Nullable) - Used for password reset
- `tokenExpirationTime`: LocalDateTime (Nullable) - Token expiration

#### Hotel
- `id`: Long (Primary Key)
- `name`: String (Not Null, Unique)
- `city`: String (Not Null)
- `room`: One-to-Many relationship with Room entity

#### Room
- `id`: Long (Primary Key)
- `roomType`: String (Not Null)
- `roomNumber`: String (Not Null)
- `view`: String (Not Null)
- `bedType`: String (Not Null)
- `accessibility`: String (Not Null)
- `pricePerNight`: BigDecimal (Not Null)
- `description`: String (Not Null)
- `checkIn`: LocalDate (Nullable)
- `checkOut`: LocalDate (Nullable)
- `hotel`: Many-to-One relationship with Hotel entity

### Relationships
- One Hotel can have many Rooms (One-to-Many)
- One Room belongs to one Hotel (Many-to-One)

## Application Architecture

The Room8 application follows a standard multi-layered architecture:

1. **Controller Layer**: Handles HTTP requests and responses
2. **Service Layer**: Contains business logic
3. **Repository Layer**: Manages data access
4. **Model Layer**: Represents data entities
5. **DTO Layer**: Data Transfer Objects for API communication
6. **Security Layer**: Manages authentication and authorization

### Package Structure
```
Room8.Back
├── BackApplication.java              # Main application class
├── hotel                             # Hotel module
│   ├── controller                    # REST controllers for hotel operations
│   ├── dto                           # DTOs for hotel data
│   ├── model                         # Hotel-related entities
│   ├── repository                    # Data access interfaces
│   └── service                       # Business logic for hotel operations
├── security                          # Security module
│   ├── configuration                 # Security configuration classes
│   └── jwt                           # JWT implementation classes
└── user                              # User module
    ├── controller                    # REST controllers for user operations
    ├── model                         # User entity and DTOs
    ├── repository                    # User data access interfaces
    └── service                       # Business logic for user operations
```

## Security Implementation

### Authentication Flow

The application uses JWT-based authentication with tokens stored in HTTP-only cookies:

1. User submits credentials (email/password)
2. Server validates credentials against stored data
3. If valid, server generates a JWT token
4. Token is sent to client as an HTTP-only cookie
5. Client includes the cookie in subsequent requests
6. Server validates the token with each request

### Security Configuration

The `SecurityConfiguration` class configures security rules:
- Public endpoints (login, register, password reset) are accessible without authentication
- All other endpoints require authentication
- CORS is configured to allow requests from the frontend application
- Session management is set to stateless
- JWT filter is added to the filter chain

### Password Encryption

User passwords are encrypted using BCrypt before storage:
- Salt rounds are automatically handled by Spring Security
- Password comparison during login is handled by Spring Security's `DaoAuthenticationProvider`

### JWT Implementation

The JWT service handles token operations:
- Token generation with user details and expiration time
- Token validation and parsing
- Username extraction from tokens
- Token blacklisting for logout functionality

## API Endpoints

### Authentication Endpoints

#### Registration
- **URL**: `/api/auth/register`
- **Method**: POST
- **Request Body**: 
  ```json
  {
    "fullName": "string",
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: JWT token and user information
- **Authorization**: None

#### Login
- **URL**: `/api/auth/login`
- **Method**: POST
- **Request Body**: 
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: JWT token and user information
- **Authorization**: None

#### Logout
- **URL**: `/api/auth/logout`
- **Method**: POST
- **Response**: Success message
- **Authorization**: Required

### User Management Endpoints

#### Get User Information
- **URL**: `/user/information`
- **Method**: GET
- **Response**: User profile information
- **Authorization**: Required

#### Send Password Reset Email
- **URL**: `/user/send-email-recover-password`
- **Method**: POST
- **Request Body**: 
  ```json
  {
    "email": "string"
  }
  ```
- **Response**: No content
- **Authorization**: None

#### Reset Password with Token
- **URL**: `/user/reset-password`
- **Method**: PUT
- **Request Parameters**: 
  - `token`: Reset token
  - `newPassword`: New password
  - `confirmPassword`: Password confirmation
- **Response**: Success or error message
- **Authorization**: None

#### Reset Password when Logged In
- **URL**: `/user/reset-password-profile`
- **Method**: PUT
- **Request Parameters**: 
  - `newPassword`: New password
  - `confirmPassword`: Password confirmation
- **Response**: Success or error message
- **Authorization**: Required

### Hotel Endpoints

#### Get All Hotels Overview
- **URL**: `/hotel/overview`
- **Method**: GET
- **Response**: List of hotels with room counts
- **Authorization**: Required

#### Search Hotels by Reservation Criteria
- **URL**: `/hotel/search/reservation`
- **Method**: GET
- **Request Body**: 
  ```json
  {
    "city": "string",
    "checkIn": "yyyy-MM-dd",
    "checkOut": "yyyy-MM-dd",
    "nbPerson": "integer",
    "nbRoom": "integer"
  }
  ```
- **Response**: List of available hotels matching criteria
- **Authorization**: Required

## Service Layer

The service layer contains the business logic for the application:

### AuthenticationService

Handles user registration and authentication:
- `register(RegisterRequestDto)`: Registers a new user
- `login(LoginRequestDto)`: Authenticates a user
- `logout(HttpServletResponse)`: Handles user logout

### UserService

Manages user-related operations:
- `userInformation()`: Gets current user's profile information
- `forgotPassword(UserMail)`: Initiates password reset process
- `resetPassword(String, String, String)`: Resets password with token
- `resetPasswordProfile(String, String)`: Changes password when logged in

### HotelService

Handles hotel-related operations:
- `getAllHotelPreviews()`: Gets hotel overview information
- `getHotelBySearchReservation(ReservationDTO)`: Searches for available hotels

### JwtService

Manages JWT token operations:
- `generateToken(UserDetails)`: Generates JWT token
- `isTokenValid(String, UserDetails)`: Validates token
- `extractUsername(String)`: Extracts username from token
- `addTokenToBlacklist(String)`: Blacklists a token for logout

## Data Access Layer

The data access layer is implemented using Spring Data JPA repositories:

### UserRepository
- `findByEmail(String)`: Finds user by email
- `findByResetToken(String)`: Finds user by reset token

### HotelRepository
- `findByName(String)`: Finds hotel by name
- `findByCity(String)`: Finds hotels by city
- `findAll()`: Gets all hotels

### RoomRepository
- `findByHotelCity(String)`: Finds rooms by hotel city
- `findAll()`: Gets all rooms

## Email Service

The application uses Spring Mail for sending emails:

### Configuration
- SMTP server: smtp.gmail.com
- Port: 587
- Username: room8msc@gmail.com
- Authentication: Enabled
- STARTTLS: Enabled

### Email Templates
- **Password Reset Email**:
  - Subject: "Réinitialisation de votre mot de passe Room8"
  - Content: Reset link with token

## Configuration

### Application Properties
```properties
# Server Configuration
app.url = http://localhost:3000

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/room8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=false
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always

# Security Configuration
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=86400000

# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=room8msc@gmail.com
spring.mail.password=ilrfyokgpmvlozce
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## JWT Authentication Flow

### Token Generation
1. User authenticates with valid credentials
2. Server creates JWT token with:
   - Subject: User's email
   - Issued at: Current time
   - Expiration: Current time + expiration period (24 hours)
   - Signature: HMAC-SHA256 with secret key
3. Token is stored in an HTTP-only cookie

### Request Authentication
1. `JwtAuthenticationFilter` intercepts incoming requests
2. Filter extracts JWT token from the cookie
3. Token is validated (expiration, signature)
4. User details are loaded and authenticated
5. Authentication is set in the `SecurityContextHolder`

### Token Validation
1. Extract username from token
2. Load user details by username
3. Check if token is valid:
   - Username in token matches loaded user
   - Token is not expired
   - Token is not blacklisted

## Hotel Reservation Logic

### Room Availability Check
The system determines room availability based on:
1. Existing reservations (check-in and check-out dates)
2. Requested reservation period
3. Location (city)

A room is considered available if:
- It has no existing reservation (check-in and check-out are null)
- OR the requested period doesn't overlap with existing reservation:
  - Requested check-out is before existing check-in
  - OR requested check-in is after existing check-out

### Hotel Search Algorithm
1. Find hotels in the requested city
2. For each hotel, find all rooms
3. Check each room for availability during the requested period
4. Create hotel overview with available room count
5. Return list of hotels with available rooms

## Error Handling

The application uses Spring's exception handling mechanism:

### Authentication Errors
- Invalid credentials: "Invalid email or password"
- Registration errors: "Error registering user"

### Password Reset Errors
- Invalid token: "Token invalide ou expiré"
- Expired token: "Token expiré"
- Password mismatch: "Les mots de passe ne correspondent pas"

## Testing Strategy

The application should be tested at multiple levels:

### Unit Testing
- Service layer business logic
- Authentication flows
- Repository queries
- JWT token operations

### Integration Testing
- API endpoints
- Database operations
- Email sending

### Security Testing
- Authentication bypass attempts
- JWT token validation
- Password encryption
- CORS configuration
