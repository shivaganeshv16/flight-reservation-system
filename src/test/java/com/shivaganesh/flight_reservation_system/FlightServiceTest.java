package com.shivaganesh.flight_reservation_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightServiceTest {

  private FlightService flightService;
  private Flight nyFlightMorning;
  private Flight nyFlightEvening;

    @BeforeEach
    void setUp() {
    	
        flightService = new FlightService();
        nyFlightMorning = new Flight(
                "FL100",
                "New York",
                LocalDateTime.of(2025, 12, 20, 9, 0),
                10);

        nyFlightEvening = new Flight(
                "FL101",
                "New York",
                LocalDateTime.of(2025, 12, 20, 15, 30), 5);

        Flight londonFlight = new Flight(
                "FL200",
                "London",
                LocalDateTime.of(2025, 12, 21, 11, 0), 8);

        flightService.addFlight(nyFlightMorning);
        flightService.addFlight(nyFlightEvening);
        flightService.addFlight(londonFlight);
    }

    @Test
    void searchFlights_returnsMatchingFlightsByDestinationAndDate() {
        LocalDateTime searchDate = LocalDateTime.of(2025, 12, 20, 0, 0);
        List<Flight> results = flightService.searchFlights("New York", searchDate);

        assertEquals(2, results.size());
        assertTrue(results.contains(nyFlightMorning));
        assertTrue(results.contains(nyFlightEvening));
    }

    @Test
    void searchFlights_returnsEmptyListWhenNoMatches() {
        LocalDateTime searchDate = LocalDateTime.of(2025, 12, 25, 0, 0);
        List<Flight> results = flightService.searchFlights("New York", searchDate);

        assertTrue(results.isEmpty());
    }

    @Test
    void bookFlight_reducesAvailableSeatsAndCreatesReservation() {
        int seatsToBook = 3;
        int initialSeats = nyFlightMorning.getAvailableSeats();

        Reservation reservation = flightService.bookFlight("Alice", nyFlightMorning, seatsToBook);

        assertNotNull(reservation);
        assertEquals("Alice", reservation.getCustomerName());
        assertEquals(seatsToBook, reservation.getSeatsBooked());
        assertEquals(initialSeats - seatsToBook, nyFlightMorning.getAvailableSeats());
        assertTrue(flightService.getAllReservations().contains(reservation));
    }

    @Test
    void bookFlight_throwsExceptionWhenNotEnoughSeats() {
        int seatsToBook = 20;
        int initialSeats = nyFlightMorning.getAvailableSeats();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> flightService.bookFlight("Bob", nyFlightMorning, seatsToBook)
        );

        assertTrue(ex.getMessage().contains("Not enough seats"));
        assertEquals(initialSeats, nyFlightMorning.getAvailableSeats());
        assertTrue(flightService.getAllReservations().isEmpty());
    }

    @Test
    void findReservationsByCustomer_returnsOnlyMatchingReservations() {
        flightService.bookFlight("Charlie", nyFlightMorning, 2);
        flightService.bookFlight("Charlie", nyFlightEvening, 1);
        flightService.bookFlight("Dana", nyFlightEvening, 1);

        List<Reservation> charliesReservations =
                flightService.findReservationsByCustomer("Charlie");

        assertEquals(2, charliesReservations.size());
        assertTrue(charliesReservations.stream()
                .allMatch(r -> r.getCustomerName().equals("Charlie")));
    }
    
    // destination with case sensitive
    @Test
    void searchFlights_isCaseInsensitiveForDestination() {
        LocalDateTime searchDate = LocalDateTime.of(2025, 12, 20, 0, 0);

        List<Flight> results = flightService.searchFlights("new york", searchDate);

        assertEquals(2, results.size());
        assertTrue(results.contains(nyFlightMorning));
        assertTrue(results.contains(nyFlightEvening));
    }
    
 // fully booked flight should not be return
    @Test
    void searchFlights_excludesFlightsWithNoAvailableSeats() {
        nyFlightMorning.setAvailableSeats(0); 

        LocalDateTime searchDate = LocalDateTime.of(2025, 12, 20, 0, 0);
        List<Flight> results = flightService.searchFlights("New York", searchDate);

        assertEquals(1, results.size(), "Only one flight should be returned");
        assertFalse(results.contains(nyFlightMorning));
        assertTrue(results.contains(nyFlightEvening));
    }
    
    //booking reject invalid seat counts
    @Test
    void bookFlight_throwsWhenSeatsAreZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> flightService.bookFlight("Alice", nyFlightMorning, 0));

        assertThrows(IllegalArgumentException.class,
                () -> flightService.bookFlight("Alice", nyFlightMorning, -1));
    }
//testing blank customer name
    @Test
    void bookFlight_throwsWhenCustomerNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> flightService.bookFlight("   ", nyFlightMorning, 1));
    }
    
    @Test
    void bookFlight_throwsWhenFlightNotManagedByService() {
        Flight otherFlight = new Flight(
                "XX999",
                "New York",
                LocalDateTime.of(2025, 12, 20, 18, 0),
                5
        );

        assertThrows(IllegalArgumentException.class,
                () -> flightService.bookFlight("Alice", otherFlight, 1));
    }


}
