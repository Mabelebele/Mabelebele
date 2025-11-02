# Mobile Car Wash Backend API

A complete backend solution for a mobile car wash application built with Spring Boot, Java 21, and PostgreSQL.

## Features

- **User Management**: Registration, login, and authentication with JWT
- **Vehicle Management**: Add, update, and manage customer vehicles
- **Service Management**: Create and manage car wash services
- **Booking System**: Book car wash services with location and scheduling
- **Payment Processing**: Handle payments for bookings
- **Role-based Access Control**: Customer, Service Provider, and Admin roles
- **RESTful APIs**: Comprehensive REST endpoints
- **API Documentation**: Swagger/OpenAPI integration
- **Security**: JWT-based authentication and authorization
- **Logging**: Comprehensive logging throughout the application
- **Unit Testing**: JUnit and Mockito tests

## Tech Stack

- **Java**: 21
- **Spring Boot**: 3.2.0
- **Spring Security**: JWT Authentication
- **Spring Data JPA**: Database operations
- **PostgreSQL**: Primary database
- **Maven**: Build tool
- **Lombok**: Reduce boilerplate code
- **Swagger/OpenAPI**: API documentation
- **JUnit 5 & Mockito**: Testing

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+ (pgAdmin recommended)
- IntelliJ IDEA (or any Java IDE)
- Postman (for API testing)

## Database Setup

1. Install PostgreSQL and pgAdmin
2. Create a new database:
   ```sql
   CREATE DATABASE carwashdb;
   ```

3. Update database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/carwashdb
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

## How to Run

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd mobile-car-wash-backend
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   
   Or run from IntelliJ:
   - Open the project in IntelliJ IDEA
   - Right-click on `MobileCarWashApplication.java`
   - Select "Run MobileCarWashApplication"

4. **Access the application**:
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/api/swagger-ui.html`
   - API Docs: `http://localhost:8080/api/api-docs`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Users
- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users/role/{role}` - Get users by role
- `DELETE /api/users/{id}` - Delete user (Admin only)

### Vehicles
- `POST /api/vehicles` - Create a vehicle
- `GET /api/vehicles` - Get all vehicles (Admin only)
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `GET /api/vehicles/user/{userId}` - Get vehicles by user
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle

### Services
- `POST /api/services` - Create a service (Admin only)
- `GET /api/services` - Get all services
- `GET /api/services/active` - Get active services
- `GET /api/services/{id}` - Get service by ID
- `GET /api/services/category/{category}` - Get services by category
- `PUT /api/services/{id}` - Update service (Admin only)
- `DELETE /api/services/{id}` - Delete service (Admin only)

### Bookings
- `POST /api/bookings` - Create a booking
- `GET /api/bookings` - Get all bookings (Admin only)
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/customer/{customerId}` - Get bookings by customer
- `GET /api/bookings/provider/{serviceProviderId}` - Get bookings by service provider
- `GET /api/bookings/status/{status}` - Get bookings by status
- `PUT /api/bookings/{id}/status` - Update booking status
- `PUT /api/bookings/{bookingId}/assign/{serviceProviderId}` - Assign service provider
- `DELETE /api/bookings/{id}` - Delete booking

### Payments
- `POST /api/payments` - Create a payment
- `GET /api/payments` - Get all payments (Admin only)
- `GET /api/payments/{id}` - Get payment by ID
- `GET /api/payments/booking/{bookingId}` - Get payment by booking
- `GET /api/payments/status/{status}` - Get payments by status
- `PUT /api/payments/{id}/status` - Update payment status
- `DELETE /api/payments/{id}` - Delete payment (Admin only)

## Enumerations

### User Roles
- `CUSTOMER`
- `SERVICE_PROVIDER`
- `ADMIN`

### Vehicle Types
- `SEDAN`
- `SUV`
- `TRUCK`
- `VAN`
- `COUPE`
- `HATCHBACK`

### Service Categories
- `BASIC_WASH`
- `PREMIUM_WASH`
- `INTERIOR_CLEANING`
- `EXTERIOR_DETAILING`
- `FULL_DETAILING`
- `SPECIALTY_SERVICE`

### Booking Status
- `PENDING`
- `CONFIRMED`
- `ASSIGNED`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`

### Payment Methods
- `CREDIT_CARD`
- `DEBIT_CARD`
- `MOBILE_MONEY`
- `CASH`
- `WALLET`

### Payment Status
- `PENDING`
- `PROCESSING`
- `COMPLETED`
- `FAILED`
- `REFUNDED`

## Testing with Postman

1. **Register a new user**:
   ```
   POST http://localhost:8080/api/auth/register
   ```
   Body:
   ```json
   {
     "email": "customer@example.com",
     "password": "password123",
     "firstName": "John",
     "lastName": "Doe",
     "phoneNumber": "1234567890",
     "role": "CUSTOMER"
   }
   ```

2. **Login**:
   ```
   POST http://localhost:8080/api/auth/login
   ```
   Body:
   ```json
   {
     "email": "customer@example.com",
     "password": "password123"
   }
   ```
   
   Copy the `token` from the response.

3. **Use JWT token for authenticated requests**:
   - In Postman, go to the Authorization tab
   - Select "Bearer Token"
   - Paste the token from login response

4. **Create a vehicle**:
   ```
   POST http://localhost:8080/api/vehicles
   Authorization: Bearer <your-token>
   ```
   Body:
   ```json
   {
     "make": "Toyota",
     "model": "Camry",
     "year": 2022,
     "color": "Black",
     "licensePlate": "ABC123",
     "vehicleType": "SEDAN"
   }
   ```

## Running Tests

```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/com/mobilecarwash/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── entity/           # JPA entities
│   │   ├── exception/        # Custom exceptions and handlers
│   │   ├── repository/       # JPA repositories
│   │   ├── security/         # Security and JWT configuration
│   │   └── service/          # Business logic
│   │       └── impl/         # Service implementations
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/mobilecarwash/
        ├── controller/       # Controller tests
        └── service/          # Service tests
```

## Logging

Logs are stored in `logs/application.log` and also output to the console. Log levels can be configured in `application.properties`.

## Security

- JWT tokens expire after 24 hours (configurable)
- Passwords are encrypted using BCrypt
- Role-based access control for endpoints
- CORS configuration for frontend integration

## Future Enhancements

- Email notifications
- SMS notifications
- Real-time tracking
- Rating and review system
- Loyalty program
- Advanced payment gateway integration
- Multi-language support
- Push notifications

## License

This project is for demonstration purposes.

## Contact

For any queries, please contact support@mobilecarwash.com
