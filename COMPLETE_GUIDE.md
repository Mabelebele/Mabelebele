# Mobile Car Wash Backend - Complete Guide

## 🚀 What You Have

A **production-ready Spring Boot backend** for a mobile car wash application with:

✅ **Complete API Backend** (50+ Java files)
- User authentication & authorization (JWT)
- Vehicle management
- Service catalog
- Booking system
- Payment processing

✅ **All Required Components**
- Entities (JPA)
- DTOs (Data Transfer Objects)
- Controllers (REST APIs)
- Services & Service Implementations
- Repositories
- Security Configuration
- Exception Handling
- Logging
- Unit Tests

✅ **Documentation & Tools**
- Swagger/OpenAPI UI
- Postman Collection
- Database Scripts
- Setup Guides

---

## 📋 Prerequisites

### 1. Java Development Kit (JDK)
- **Java 17 or higher** is required
- Verify: `java -version`
- Download from: https://adoptium.net/

### 2. PostgreSQL Database
- **PostgreSQL 12+** recommended
- Download from: https://www.postgresql.org/download/
- **pgAdmin** included in installer

### 3. IntelliJ IDEA (Recommended)
- **IntelliJ IDEA Community Edition** (free)
- Download from: https://www.jetbrains.com/idea/download/
- Or use any Java IDE

### 4. Postman (API Testing)
- Download from: https://www.postman.com/downloads/

---

## 🔧 Setup Instructions

### Step 1: Database Setup

1. **Start PostgreSQL**
   - Ensure PostgreSQL service is running

2. **Create Database**
   - Open pgAdmin
   - Right-click "Databases" → Create → Database
   - Name: `carwashdb`
   - Owner: `postgres`
   - Click "Save"

3. **Configure Connection**
   - Open `src/main/resources/application.properties`
   - Update these lines:
   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_POSTGRES_PASSWORD
   ```

### Step 2: Build & Run

#### Option A: Using IntelliJ IDEA (Easiest)

1. **Open Project**
   - File → Open
   - Select the project folder
   - Wait for Maven to download dependencies (first time only)

2. **Run Application**
   - Navigate to: `src/main/java/com/mobilecarwash/MobileCarWashApplication.java`
   - Right-click → Run 'MobileCarWashApplication'
   - Wait for "Started MobileCarWashApplication" message

3. **Verify**
   - Console should show: `Tomcat started on port(s): 8080`
   - No errors in console

#### Option B: Using Command Line

```bash
# Navigate to project directory
cd mobile-car-wash-backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Step 3: Verify Application is Running

Open browser and visit:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- You should see the API documentation interface

---

## 🧪 Testing the API

### Method 1: Using Postman (Recommended)

1. **Import Collection**
   - Open Postman
   - Click "Import"
   - Select `Mobile_Car_Wash_API.postman_collection.json`

2. **Test Flow**

   **A. Register a Customer**
   ```
   POST http://localhost:8080/api/auth/register
   ```
   - Select "Register Customer" request
   - Click "Send"
   - Should get 201 Created

   **B. Login**
   ```
   POST http://localhost:8080/api/auth/login
   ```
   - Select "Login" request
   - Click "Send"
   - Copy the `token` from response (auto-saved)

   **C. Create a Vehicle**
   ```
   POST http://localhost:8080/api/vehicles
   ```
   - Token is automatically added to header
   - Click "Send"
   - Should get 201 Created

   **D. View Services**
   ```
   GET http://localhost:8080/api/services/active
   ```
   - Click "Send"
   - See available services

   **E. Create a Booking**
   ```
   POST http://localhost:8080/api/bookings
   ```
   - Update `vehicleId` and `serviceId` with actual IDs
   - Click "Send"

   **F. Create a Payment**
   ```
   POST http://localhost:8080/api/payments
   ```
   - Update `bookingId` with actual booking ID
   - Click "Send"

### Method 2: Using Swagger UI

1. **Open Swagger**
   - http://localhost:8080/api/swagger-ui.html

2. **Register**
   - Expand "Authentication" → POST /auth/register
   - Click "Try it out"
   - Enter user details
   - Click "Execute"

3. **Login & Get Token**
   - POST /auth/login
   - Copy the `token` from response

4. **Authorize**
   - Click "Authorize" button (top right)
   - Enter: `Bearer YOUR_TOKEN`
   - Click "Authorize"

5. **Test Other Endpoints**
   - All endpoints are now accessible
   - Try creating vehicles, bookings, etc.

### Method 3: Using cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "0123456789",
    "role": "CUSTOMER"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# Use the token for authenticated requests
curl -X GET http://localhost:8080/api/vehicles \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📊 Database Monitoring

### Using pgAdmin

1. **View Tables**
   - pgAdmin → Databases → carwashdb → Schemas → public → Tables
   - Tables created: users, vehicles, services, bookings, payments

2. **View Data**
   - Right-click table → View/Edit Data → All Rows

3. **Run Queries**
   ```sql
   -- View all users
   SELECT * FROM users;
   
   -- View all bookings with details
   SELECT b.id, b.scheduled_date_time, b.status, 
          u.email as customer, s.name as service
   FROM bookings b
   JOIN users u ON b.customer_id = u.id
   JOIN services s ON b.service_id = s.id;
   ```

---

## 🔐 User Roles & Permissions

### Customer Role
- Create and manage own vehicles
- Create bookings
- Create payments
- View own data

### Service Provider Role
- View assigned bookings
- Update booking status
- View customer details for assignments

### Admin Role
- Full access to all endpoints
- Manage services
- Manage users
- View all bookings and payments

---

## 📝 API Endpoints Summary

### Authentication (No Auth Required)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Users (Authenticated)
- `GET /api/users` - Get all users (Admin)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users/role/{role}` - Get users by role (Admin)

### Vehicles (Authenticated)
- `POST /api/vehicles` - Create vehicle
- `GET /api/vehicles` - Get all vehicles (Admin)
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `GET /api/vehicles/user/{userId}` - Get user's vehicles
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle

### Services (Authenticated)
- `POST /api/services` - Create service (Admin)
- `GET /api/services` - Get all services
- `GET /api/services/active` - Get active services
- `GET /api/services/{id}` - Get service by ID
- `PUT /api/services/{id}` - Update service (Admin)
- `DELETE /api/services/{id}` - Deactivate service (Admin)

### Bookings (Authenticated)
- `POST /api/bookings` - Create booking
- `GET /api/bookings` - Get all bookings (Admin)
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/customer/{id}` - Get customer bookings
- `GET /api/bookings/provider/{id}` - Get provider bookings
- `PUT /api/bookings/{id}/status` - Update status
- `PUT /api/bookings/{id}/assign/{providerId}` - Assign provider

### Payments (Authenticated)
- `POST /api/payments` - Create payment
- `GET /api/payments` - Get all payments (Admin)
- `GET /api/payments/{id}` - Get payment by ID
- `GET /api/payments/booking/{id}` - Get payment by booking
- `PUT /api/payments/{id}/status` - Update payment status

---

## 🐛 Troubleshooting

### Application won't start

**Issue**: Port 8080 already in use
```
Solution: Change port in application.properties
server.port=8081
```

**Issue**: Cannot connect to PostgreSQL
```
Solution:
1. Check PostgreSQL is running
2. Verify username/password in application.properties
3. Ensure database 'carwashdb' exists
```

**Issue**: Build errors
```
Solution:
1. Run: mvn clean install
2. Check Java version: java -version (need 17+)
3. Delete .m2 folder and rebuild
```

### Tests failing
```bash
# Run tests
mvn test

# Skip tests during build
mvn clean install -DskipTests
```

### JWT token issues

**Issue**: Token expired
```
Solution: Login again to get new token
Tokens expire after 24 hours (configurable)
```

---

## 📈 Next Steps for Production

1. **Payment Gateway Integration**
   - Integrate Stripe, PayPal, or PayFast
   - Update PaymentServiceImpl

2. **Notifications**
   - Add email notifications (JavaMailSender)
   - Add SMS notifications (Twilio)

3. **Enhanced Features**
   - Real-time tracking (WebSockets)
   - Rating and reviews system
   - Loyalty program
   - Promo codes

4. **Deployment**
   - Deploy to AWS, Azure, or Heroku
   - Set up CI/CD pipeline
   - Configure production database

5. **Monitoring**
   - Add Prometheus metrics
   - Set up ELK stack for logs
   - Configure alerts

---

## 💰 Monetization Strategy

The backend supports these monetization features:

1. **Service Fees**
   - Each service has a base price
   - Price variations by vehicle type (extensible)

2. **Commission Model**
   - Track service provider earnings
   - Calculate platform commission

3. **Subscription Plans**
   - Add subscription entity
   - Implement recurring billing

4. **Dynamic Pricing**
   - Time-based pricing (peak hours)
   - Location-based pricing
   - Demand-based pricing

---

## 📞 Support

- Check logs: `logs/application.log`
- View console output in IntelliJ
- Check Swagger UI for API documentation

## 📄 License

This project is for demonstration and educational purposes.

---

## ✅ Quick Checklist

- [ ] PostgreSQL installed and running
- [ ] Database `carwashdb` created
- [ ] Application.properties configured
- [ ] Application starts successfully
- [ ] Swagger UI accessible
- [ ] Postman collection imported
- [ ] Can register and login
- [ ] Can create vehicles
- [ ] Can create bookings
- [ ] Can create payments

**Congratulations! Your mobile car wash backend is ready to use! 🎉**
