# Railway Station Management System

## Project Overview

This application provides a comprehensive management system for railway operations, designed for both administrative staff and customers. It simulates a complete railway station ecosystem with real-time schedule management, ticketing, route planning, and user account management.

## Core Features

### Administrator Portal
- **Station Management**: Add, view, and manage railway stations and their platforms
- **Train Fleet Management**: Register and maintain information about trains, their types, and capacities
- **Route Planning**: Create and price routes between stations
- **Schedule Configuration**: Schedule trains on routes with platform assignment and timing
- **Database Administration**: Clear and reset database when needed
- **Revenue Analytics**: View comprehensive revenue reports from ticket sales

### Customer Portal
- **Journey Planning**: Browse available routes and train schedules
- **Ticket Purchasing**: Buy standard or first-class tickets with dynamic pricing
- **Seat Reservation**: Reserve specific seats on scheduled trains
- **Account Management**: Register and maintain personal account
- **Ticket History**: Access all previously purchased tickets

## Technical Requirements

- **Java**: JDK 11 or higher
- **Database**: PostgreSQL 12 or higher
- **JDBC Driver**: PostgreSQL JDBC Driver
- **Memory**: 2GB RAM minimum
- **Storage**: 100MB available disk space

## Installation Guide

### 1. Database Setup

#### Using psql:
```sql
CREATE USER mihnea2 WITH PASSWORD 'mihnea2';
CREATE DATABASE "Proiect_PAO";
GRANT ALL PRIVILEGES ON DATABASE "Proiect_PAO" TO mihnea2;
```

#### Using pgAdmin (recomended):
1. Create a new database named `Proiect_PAO`
2. Create a user `mihnea2` with password `mihnea2`
3. Grant all privileges to this user on the database

### 2. JDBC Driver Configuration

1. Download the PostgreSQL JDBC driver from [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/download/)
2. Create a `lib` folder in your project root
3. Add the downloaded JAR file to this folder
4. Configure your IDE to include the JAR in your project classpath

### 3. Application Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/MihneaVe/Train_Go_Brr_2.git
   ```

2. Navigate to the project directory:
   ```bash
   cd railway-station-management
   ```

## Running the Application

### From IDE:
Run the `Main.java` class from your IDE

## Default Accounts

The system comes with pre-configured accounts for testing:

### Administrator Account
- **Username**: admin
- **Password**: admin123

### Sample Customer Account
- **Username**: john
- **Password**: pass123

## System Architecture

The application follows a layered architecture:

1. **Presentation Layer**: 
   - Menu-driven console interface
   - Input validation and user interaction

2. **Service Layer**:
   - Business logic implementation
   - User authentication and authorization
   - Ticket price calculation and scheduling

3. **Repository Layer**:
   - Data access and persistence
   - CRUD operations for all entities

4. **Model Layer**:
   - Core domain entities (Train, Station, Ticket, etc.)
   - Business rules and relationships

## Database Schema

The application uses the following relational database schema:

### Tables
- **users**: User accounts with role-based permissions
- **stations**: Railway stations with platform information
- **trains**: Train information including type and capacity
- **routes**: Connections between stations with pricing
- **schedules**: Train assignments to routes with timing details

### Relationships
- A route connects exactly two stations (origin and destination)
- A schedule assigns one train to one route at specific times
- A ticket is purchased by one customer for one schedule
- A reservation is made by one customer for one schedule and seat

## Security Features

- Password complexity requirements enforced
- Admin-only operations protected
- Input validation on all user inputs
- Password confirmation for sensitive operations

## Audit & Logging

All system actions are logged to `audit.csv` with timestamps for compliance and security monitoring.

## Error Handling

The application implements comprehensive error handling:
- Database connection issues gracefully managed
- Input validation with user-friendly error messages
- Option to return to previous menus from any point
- Prevention of data inconsistencies

## Extending the System

The modular design allows for easy extension:
- New entity types can be added by creating model, repository, and service classes
- Additional user roles can be implemented by extending the User class
- New report types can be added to the service layer

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify PostgreSQL is running
   - Check credentials in DatabaseService.java
   - Ensure the JDBC driver is correctly added to classpath

2. **Cannot Login**
   - Default admin: username "admin", password "pass123"
   - Check case sensitivity of credentials
   - Verify the database tables were created successfully

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributors

- Velcea Mihnea-Andrei
- This `README.md` file has been formatted with the help of AI (Claude 3.7 Sonnet)
