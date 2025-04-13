# README - Railway Station Management System

## General Description

The application represents a management system for a railway station that allows the administration of trains, routes, stations, and tickets. The system facilitates common operations in a station, such as:

- Purchasing tickets
- Managing routes
- Scheduling trains
- User administration

---

## Application Structure

### Package `pkcg.model`

This package contains the model classes that represent the core entities of the system:

- **`User` (abstract class)**  
    - Base class for authenticated users  
    - Contains `username` and `password` with authentication methods  

- **`Admin` (extends `User`)**  
    - Represents system administrators  
    - Has elevated rights such as modifying prices or adding trains  

- **`Customer` (extends `User`)**  
    - Represents system customers  
    - Contains personal data (`full name`, `email`)  
    - Can purchase tickets and make reservations  

- **`Station`**  
    - Represents a train station  
    - Includes the station name and available platforms  
    - Manages the list of platforms in the station  

- **`Platform`**  
    - Represents a platform in the station  
    - Has a unique identification number  

- **`Train`**  
    - Represents a train  
    - Includes `number`, `type`, and `capacity`  

- **`Route`**  
    - Represents a route between two stations  
    - Contains `departure station`, `destination station`, and `base price`  

- **`Schedule`**  
    - Represents the scheduling of a train on a specific route  
    - Includes `train`, `route`, `departure time`, `arrival time`, and `platform number`  

- **`Ticket`**  
    - Represents a travel ticket  
    - Contains details about the `customer`, `schedule`, `price`, and `class` (first class or not)  

- **`Reservation`**  
    - Represents a seat reservation  
    - Includes `customer`, `schedule`, `seat number`, and `confirmation status`  

---

### Package `pkcg.service`

This package contains the classes that implement business logic:

- **`UserService`**  
    - Manages system users  
    - Allows registration of administrators and customers  
    - Provides authentication and authorization functionalities  
    - Maintains the current user session  

- **`StationService`**  
    - Manages stations, trains, routes, and schedules  
    - Allows adding and managing stations  
    - Allows adding and managing trains  
    - Provides search functionalities by destination station  
    - Manages routes and updates prices  
    - Handles seat reservations  

- **`TicketService`**  
    - Manages ticket purchasing and administration  
    - Allows purchasing tickets for a schedule  
    - Provides reports on sold tickets  
    - Calculates total revenue  

---

### Class `pkcg.Main`

The main class that demonstrates the system's functionalities:

- Initializes the necessary services  
- Creates test data (`stations`, `trains`, `routes`, `schedules`, `users`)  
- Demonstrates available operations for administrators and customers  

---

## Available Operations

### Registration and Authentication

- Register administrators and customers with personal data  
- Log in to the system with `username` and `password`  

### Station Management

- Add new stations with a number of platforms  
- Retrieve the list of available stations  

### Train Management

- Add new trains with details about type and capacity  
- View available trains  

### Route Administration

- Create routes between stations with base prices  
- Update prices for routes (admin only)  
- View available routes  

### Schedule Management

- Add schedules for trains on specific routes  
- Search schedules by destination  
- View all schedules  

### Ticket Administration

- Purchase tickets for a schedule  
- Select travel class (`standard` or `first class`)  
- View tickets for a customer  

### Reservations

- Reserve seats for a schedule  
- Confirm reservations  
- Cancel reservations  

### Reports

- Calculate total revenue from ticket sales  

---

## Usage Example

The application demonstrates through the `Main` class:

### Administrative Operations:

1. Log in as an administrator  
2. Modify prices for routes  
3. View updated routes  

### Customer Operations:

1. Log in as a customer  
2. View available routes  
3. Purchase a travel ticket  