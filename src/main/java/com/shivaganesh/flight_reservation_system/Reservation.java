package com.shivaganesh.flight_reservation_system;

import java.util.Objects;

public class Reservation {

    private final String customerName;
    private final Flight flight;
    private final int seatsBooked;

    public String getCustomerName() {
        return customerName;
    }
    public Flight getFlight() {
        return flight;
    }

    public int getSeatsBooked() {
    	
        return seatsBooked;
    }
    // validations
    public Reservation(String customerName, Flight flight, int seatsBooked) {
    	
        if (customerName == null || customerName.isBlank()) {
        	
            throw new IllegalArgumentException("Err: customerName must not be blank");
        }
        if (flight == null) {
        	
            throw new IllegalArgumentException("Err: flight must not be null");
        }
        if (seatsBooked <= 0) {
            throw new IllegalArgumentException("Err: seatsBooked must be > 0");
        }
        
        this.customerName = customerName;
        this.flight = flight;
        this.seatsBooked = seatsBooked;
    }

    // for readable text in console output
    @Override
    public String toString() {
        return "Reservation{" +
                "customerName='" + customerName + '\'' +
                ", flightNumber='" + flight.getFlightNumber() + '\'' +
                ", destination='" + flight.getDestination() + '\'' +
                ", departureTime=" + flight.getDepartureTime() +
                ", seatsBooked=" + seatsBooked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
    	
        if (this == o) return true;
        
        if (!(o instanceof Reservation)) return false;
        
        Reservation that = (Reservation) o;
        return seatsBooked == that.seatsBooked && Objects.equals(customerName, that.customerName) &&
               Objects.equals(flight, that.flight);
    }

    @Override
    public int hashCode() {
    	
        return Objects.hash(customerName, flight, seatsBooked);
    }
}
