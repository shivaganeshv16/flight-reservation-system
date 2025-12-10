# Flight Reservation System 

## 1 Overview

This project is a console-based Flight Reservation System written in Java 17.

The application allows a user to:

- Search flights by destination and date
- Book seats on a selected flight
- View all reservations for a customer

All data is stored temporary in memory because the program does not use database.

## 2 Tech Stack
- Java 17  
- Maven  
- JUnit 5 (Jupiter)

Main class: `com.shivaganesh.flight_reservation_system.FlightReservationApp`

## 3 Build & Run

### Prerequisites

- Java 17 (`java -version`)
- Maven  (`mvn -version`)

### Run tests
 in project folder open bash command
`mvn clean test` This runs the unit tests for `FlightService`.

`mvn clean package` 

Run the console application

`java -cp target/flight-reservation-system-0.0.1-SNAPSHOT.jar   com.shivaganesh.flight_reservation_system.FlightReservationApp`

On starting app you will see something like:(showing flights list initially)

Sample flights loaded:
Flight{flightNumber='FL101', destination='New York', ...}
Flight{flightNumber='FL102', destination='New York', ...}
Flight{flightNumber='FL201', destination='London', ...}
Flight{flightNumber='FL301', destination='California', ...}

=== Welcome to Shiva's Flight Reservation System ===
1. Search flights by destination and date
2. Book a flight
3. View my reservations
4. Exit
Enter your choice:

You can then choose your option.

## 4 Design
## Flight class

- Fields: `flightNumber`, `destination`, `departureTime` (`LocalDateTime`), `availableSeats`
- Constructor validates:
- `flightNumber` not null/blank  
- `destination` not null/blank  
- `departureTime` not null  
- `availableSeats >= 0`
- Only `availableSeats` is mutable so bookings can reduce the seat count.
- `equals`/`hashCode` based on `flightNumber`.

## Reservation class

- Fields: `customerName`, `flight`, `seatsBooked`
- Constructor validates customer name, flight, and seat count (`seatsBooked > 0`).
- Immutable once created.
- `toString()` includes flight details for easier console output.

### Service Layer – `FlightService`

- Holds in-memory:
  - `List<Flight> flights`
  - `List<Reservation> reservations`
- `searchFlights(destination, date)`:
  - validates inputs
  - matches destination case-insensitively
  - compares only the calendar date (ignoring time)
  - excludes flights with no available seats
- `bookFlight(customerName, flight, seats)`:
  - validates inputs
  - checks flight is managed by this service
  - checks enough seats are available
  - reduces `availableSeats`
  - creates and stores a `Reservation`
- `findReservationsByCustomer(customerName)`:
  - case-insensitive match on customer name
  - returns all matching reservations

### Console App – `FlightReservationApp`

- Seeds a few sample flights (New York, London, California).
- Shows a simple menu:
  1. Search flights by destination and date  
  2. Book a flight  
  3. View my reservations  
  4. Exit
- Uses `Scanner` for user input and delegates logic to `FlightService`.

## Tests

`FlightServiceTest` covers:

- Search flight with matches and no matches
- Excluding flights with zero available seats
- Successful booking (state update + reservation stored)
- Booking more seats than available (exception, no state change)
- Case-insensitive destination search
- Invalid inputs:
- zero or negative seat counts
- blank customer names
- booking a flight not managed by the service
- Finding reservations for a specific customer name only.


## 5 Real-Life Considerations

If extended for production, this system could be improved by:

- Storing flights and reservations in a database instead of memory lists.
- Handling concurrent bookings with transactions/locking to avoid over selling seats.
- Exposing `FlightService` via REST/GraphQL APIs and building a web or mobile UI on top.
- Using structured logging and metrics instead of `System.out.println`.
- Expanding business rules (fare classes, cancellations, maximum seats per booking).
