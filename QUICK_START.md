# Quick Start Guide - Mobile Car Wash Backend

## Prerequisites Installation

### 1. Install PostgreSQL
1. Download and install PostgreSQL from https://www.postgresql.org/download/
2. Install pgAdmin (comes with PostgreSQL installer)
3. Create a database named `carwashdb`:
   - Open pgAdmin
   - Right-click on "Databases" → Create → Database
   - Database name: `carwashdb`
   - Click "Save"

### 2. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=postgres
spring.datasource.password=your_password_here
```
Replace `your_password_here` with your PostgreSQL password.

## Running the Application

### Option 1: Using IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA
2. File → Open → Select the project folder
3. Wait for Maven to download dependencies
4. Right-click on `src/main/java/com/mobilecarwash/MobileCarWashApplication.java`
5. Select "Run MobileCarWashApplication"
6. Application will start on http://localhost:8080/api

### Option 2: Using Maven Command Line
```bash
mvn spring-boot:run
```

## Verify Application is Running
1. Open browser: http://localhost:8080/api/swagger-ui.html
2. You should see the Swagger UI with all API endpoints

## Testing with Postman

### 1. Import Postman Collection
1. Open Postman
2. Click "Import" button
3. Select the file `Mobile_Car_Wash_API.postman_collection.json`
4. Collection will be imported with all endpoints

### 2. Test the API Flow

#### Step 1: Register a Customer
- Endpoint: `POST /api/auth/register`
- Use the "Register Customer" request
- Click "Send"
- You should get a 201 Created response

#### Step 2: Login
- Endpoint: `POST /api/auth/login`
- Use the "Login" request
- Click "Send"
- Copy the `token` from the response
- The token is automatically saved in Postman collection variable

#### Step 3: Create a Vehicle
- Endpoint: `POST /api/vehicles`
- Authorization header is automatically set
- Click "Send"
- You should get a 201 Created response with vehicle details

#### Step 4: Create a Service (Admin only)
First, register and login as admin:
- Register with role: "ADMIN"
- Login to get admin token
- Use "Create Service" request

#### Step 5: Create a Booking
- Endpoint: `POST /api/bookings`
- Update vehicleId and serviceId with created IDs
- Click "Send"

#### Step 6: Create a Payment
- Endpoint: `POST /api/payments`
- Update bookingId with created booking ID
- Click "Send"

## Application URLs
- Base API URL: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Documentation: http://localhost:8080/api/api-docs

## Default Test Data
After application starts, you can insert sample services by running:
```sql
-- Connect to carwashdb in pgAdmin
-- Copy and paste the INSERT statements from database-setup.sql
```

## Common Issues & Solutions

### Issue 1: Cannot connect to PostgreSQL
**Solution**: 
- Verify PostgreSQL is running
- Check username and password in application.properties
- Ensure database `carwashdb` exists

### Issue 2: Port 8080 already in use
**Solution**: 
- Change port in application.properties:
  ```properties
  server.port=8081
  ```

### Issue 3: JWT token expired
**Solution**: 
- Login again to get a new token
- Tokens expire after 24 hours

## API Testing Examples

### Register Customer
```json
POST http://localhost:8080/api/auth/register
{
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "0123456789",
  "role": "CUSTOMER"
}
```

### Login
```json
POST http://localhost:8080/api/auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```

### Create Vehicle (with Bearer token)
```json
POST http://localhost:8080/api/vehicles
Authorization: Bearer <your-token>

{
  "make": "Toyota",
  "model": "Camry",
  "year": 2022,
  "color": "Black",
  "licensePlate": "GP123ABC",
  "vehicleType": "SEDAN"
}
```

### Create Booking
```json
POST http://localhost:8080/api/bookings
Authorization: Bearer <your-token>

{
  "vehicleId": 1,
  "serviceId": 1,
  "scheduledDateTime": "2024-12-15T10:00:00",
  "address": "123 Main Street, Sandton",
  "city": "Johannesburg",
  "postalCode": "2196",
  "latitude": -26.1076,
  "longitude": 28.0567,
  "specialInstructions": "Please call when you arrive"
}
```

## Monetization Features
The app includes these monetization-ready features:
1. **Service Pricing**: Each service has a base price
2. **Booking System**: Tracks all service bookings
3. **Payment Processing**: Payment records with status tracking
4. **Commission Tracking**: Can track service provider earnings
5. **User Roles**: Separate customers, service providers, and admins

## Next Steps for Production
1. Integrate real payment gateway (Stripe, PayPal, PayFast)
2. Add email/SMS notifications
3. Implement real-time tracking
4. Add rating and review system
5. Deploy to cloud (AWS, Azure, Heroku)
6. Set up CI/CD pipeline
7. Add monitoring and logging tools

## Support
For issues or questions, check the logs in `logs/application.log`
