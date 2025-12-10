package com.shivaganesh.flight_reservation_system;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight {

   private final String flightNumber;
   private final String destination;
   private final LocalDateTime departureTime;
   private int availableSeats;

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        if (availableSeats < 0) {
            throw new IllegalArgumentException("availableSeats must be >= 0");
        }
        this.availableSeats = availableSeats;
    }
    
    // here, checking the validation
  public Flight(String flightNumber, String destination, LocalDateTime departureTime, int availableSeats) {
  if (flightNumber == null || flightNumber.isBlank()) {
      throw new IllegalArgumentException("Err: flightNumber must not be blank");
  }
  if (destination == null || destination.isBlank()) {
      throw new IllegalArgumentException("Err: destination must not be blank");
  }
  if (departureTime == null) {
      throw new IllegalArgumentException("Err: departureTime must not be null");
  }
  if (availableSeats < 0) {
      throw new IllegalArgumentException("Err: availableSeats must be >= 0");
  }
  
  this.flightNumber = flightNumber;
  this.destination = destination;
  this.departureTime = departureTime;
  this.availableSeats = availableSeats;
}

  //for returning readable text in the console for better output
    @Override
    public String toString() {
        return "Flight{" + "flightNumber='" + flightNumber + '\'' +", destination='" + destination + '\'' 
        		+", departureTime=" + departureTime +", availableSeats=" + availableSeats + '}';
    }

  //Two Flight objects are consider as equal if they have the same flight number.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight)) return false;
        Flight flight = (Flight) o;
        return Objects.equals(flightNumber, flight.flightNumber);
    }

   //flight objects that are equal has the same hash code,when using hash based collections
    @Override
    public int hashCode() {
        return Objects.hash(flightNumber);
    }
}
