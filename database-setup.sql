-- Mobile Car Wash Database Setup Script
-- PostgreSQL Database: carwashdb

-- Create database (run this as postgres superuser)
-- CREATE DATABASE carwashdb;

-- Connect to the database
\c carwashdb;

-- The tables will be auto-created by Hibernate on application startup
-- This script provides some initial data for testing

-- Wait for application to create tables first, then insert sample data

-- Sample Services
INSERT INTO services (name, description, base_price, estimated_duration, category, active, created_at, updated_at)
VALUES 
('Basic Wash', 'Exterior wash with soap and water', 50.00, 30, 'BASIC_WASH', true, NOW(), NOW()),
('Premium Wash', 'Premium exterior wash with wax', 100.00, 45, 'PREMIUM_WASH', true, NOW(), NOW()),
('Interior Cleaning', 'Complete interior vacuuming and cleaning', 120.00, 60, 'INTERIOR_CLEANING', true, NOW(), NOW()),
('Full Detailing', 'Complete interior and exterior detailing', 300.00, 180, 'FULL_DETAILING', true, NOW(), NOW()),
('Express Wash', 'Quick exterior wash', 35.00, 15, 'BASIC_WASH', true, NOW(), NOW());

-- Note: Users, vehicles, bookings, and payments will be created through the API
-- Use the Postman collection to interact with the API

-- Useful queries for monitoring

-- View all users
-- SELECT id, email, first_name, last_name, role, active FROM users;

-- View all vehicles
-- SELECT v.id, v.make, v.model, v.year, v.license_plate, u.email 
-- FROM vehicles v 
-- JOIN users u ON v.user_id = u.id;

-- View all bookings
-- SELECT b.id, b.scheduled_date_time, b.status, b.total_price,
--        u.email as customer_email, s.name as service_name
-- FROM bookings b
-- JOIN users u ON b.customer_id = u.id
-- JOIN services s ON b.service_id = s.id;

-- View all payments
-- SELECT p.id, p.amount, p.payment_method, p.status, p.transaction_id,
--        b.id as booking_id
-- FROM payments p
-- JOIN bookings b ON p.booking_id = b.id;
