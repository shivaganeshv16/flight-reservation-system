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

This is a small console app, I tried to think about how real flight booking works. A lot of this comes from my own experience booking flights on different travel sites.

- **Avoiding overbooking**  
  When we book flights in real life, we sometimes see messages like “no seats left” or “only 2 seats remaining.” I have also seen cases where flights are overbooked.  
  Because of that, in `bookFlight` I always check `availableSeats` before creating a reservation and throw an error if someone tries to book more seats than are left.

- **User-friendly search (case-insensitive, trimming)**  
  When I book tickets online, I don’t always type destinations with perfect casing, like `New York` vs `new york`.  
  So in `searchFlights`, I made the destination check case-insensitive and I trim spaces. This makes the search a bit more forgiving, similar to real booking websites.

- **Input validation to avoid weird states**  
  In real systems, bad data usually causes strange bugs later (negative seats, empty names, etc.).  
  That’s why `Flight`, `Reservation`, and `FlightService` all validate inputs: no blank customer names, no negative         seat counts, no null dates  
  I prefer to fail fast with a clear message instead of letting bad data go through.

- **Separating logic from the UI**  
  The console app (`FlightReservationApp`) just handles user input and printing to the screen.  
  All the actual logic (searching, booking, finding reservations) lives in `FlightService`.  
  This is similar to how I have built real services at work, keep the core logic separate so it’s easier to test and later plug into a REST API or another UI.

- **Simple but realistic domain model**  
  I kept `Reservation` immutable (once created, it doesn’t change), and only let `Flight` update the seat count.  
  This matches how we usually treat bookings in real life: a reservation is basically a record of what was booked, and the change happens on the flight’s seat availability.

These small choices are influenced by how real booking systems behave and by what I have seen as a user on different flight and travel sites, just simplified for this exercise.


