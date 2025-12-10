package com.shivaganesh.flight_reservation_system;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//Service to manage the flights and reservations in memory.
 
public class FlightService {
	
    private final List<Flight> flights = new ArrayList<>();
    
    private final List<Reservation> reservations = new ArrayList<>();

    public void addFlight(Flight flight) {
    	
        Objects.requireNonNull(flight, " Err: flight must not be null");
        flights.add(flight);
    }

   public List<Flight> getAllFlights() {
    	
        return Collections.unmodifiableList(flights);
    }

     public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations);
    }

     public List<Flight> searchFlights(String destination, LocalDateTime date) {

    	    if (destination == null || destination.isBlank()) {
    	        throw new IllegalArgumentException("Err: destination must not be blank");
    	    }
    	    if (date == null) {
    	        throw new IllegalArgumentException("date must not be null");
    	    }

    	    String dest = destination.trim();
    	    LocalDate requestedDate = date.toLocalDate();

    	    List<Flight> result = new ArrayList<>();

    	    for (Flight f : flights) {
    	        if (!f.getDestination().equalsIgnoreCase(dest)) {
    	            continue;
    	        }
    	        if (!f.getDepartureTime().toLocalDate().equals(requestedDate)) {
    	            continue;
    	        }
    	        if (f.getAvailableSeats() <= 0) {
    	            continue;
    	        }

    	        result.add(f);
    	    }

    	    return result;
    	}


    public Reservation bookFlight(String customerName, Flight flight, int seats) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("customerName must not be blank");
        }
        Objects.requireNonNull(flight, "flight must not be null");
        if (seats <= 0) {
            throw new IllegalArgumentException("seats must be > 0");
        }

        if (!flights.contains(flight)) {
            throw new IllegalArgumentException("Flight is not managed by this service");
        }

        int available = flight.getAvailableSeats();
        if (seats > available) {
            throw new IllegalArgumentException("Not enough seats available. Requested: "
                    + seats + ", Available: " + available);
        }

        flight.setAvailableSeats(available - seats);

        Reservation reservation = new Reservation(customerName, flight, seats);
        reservations.add(reservation);

        return reservation;
    }
    
    public List<Reservation> findReservationsByCustomer(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException(" Err: customerName must not be blank");
        }

        String normalized = customerName.trim().toLowerCase();
        List<Reservation> result = new ArrayList<>();

        for (Reservation r : reservations) {
            String reservationName = r.getCustomerName();
            if (reservationName != null &&
                    reservationName.trim().toLowerCase().equals(normalized)) {
                result.add(r);
            }
        }

        return result;
    }

}
