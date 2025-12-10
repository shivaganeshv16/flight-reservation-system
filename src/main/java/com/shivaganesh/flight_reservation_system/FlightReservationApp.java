package com.shivaganesh.flight_reservation_system;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

//Console  for interacting with the Flight Reservation System.

public class FlightReservationApp {

   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
   private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static void main(String[] args) {
	  
        FlightService flightService = new FlightService();
        myflightFlights(flightService);
        System.out.println("Sample flights loaded:");
        
        for (Flight f : flightService.getAllFlights()) {
            System.out.println(" - " + f);
        }
        System.out.println();


        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1" -> searchFlights(sc, flightService);
                    case "2" -> bookFlight(sc, flightService);
                    case "3" -> viewReservations(sc, flightService);
                    case "4" -> {
                        
                    	System.out.println("Exiting application. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

  // welcome console 
    private static void printMenu() {
      System.out.println("\n===Welcome to Shiva's Flight Reservation System ===");
      System.out.println("1. Search flights by destination and date");
      System.out.println("2. Book a flight");
      System.out.println("3. View my reservations");
      System.out.println("4. Exit");
      System.out.print("Enter your choice: ");
    }
//sample flights
    private static void myflightFlights(FlightService flightService) {
    	
    flightService.addFlight(new Flight("FL101","New York",LocalDateTime.parse("2025-12-20 09:00", DATE_TIME_FORMAT),10));
    flightService.addFlight(new Flight("FL102","New York",LocalDateTime.parse("2025-12-20 15:30", DATE_TIME_FORMAT),5));
    flightService.addFlight(new Flight("FL201","London",LocalDateTime.parse("2025-12-21 11:00", DATE_TIME_FORMAT),8));
    flightService.addFlight(new Flight("FL301","California",LocalDateTime.parse("2025-12-25 11:00", DATE_TIME_FORMAT),12));
    
    }

    
 //search flight by date and destination
    private static void searchFlights(Scanner scanner, FlightService flightService) {
        try {
            System.out.print("Enter your destination: ");
            String destination = scanner.nextLine();

            System.out.print("Enter departure date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();

            LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
            LocalDateTime dateTime = date.atStartOfDay();

            List<Flight> flights = flightService.searchFlights(destination, dateTime);

            if (flights.isEmpty()) {
            	
                System.out.println("No available flights found for that destination and date.");
            } else {
                System.out.println("--> Available flights:");
                
                for (int i = 0; i < flights.size(); i++) {
                	
                    Flight f = flights.get(i);
                    System.out.printf("%d) %s - %s at %s (Seats: %d)%n",
                            i + 1,
                            f.getFlightNumber(),
                            f.getDestination(),
                            f.getDepartureTime().format(DATE_TIME_FORMAT),
                            f.getAvailableSeats()
                    );
                }
            }
        } catch (Exception e) {
        	
            System.out.println("Error while searching flights: " + e.getMessage());
        }
    }

    private static void bookFlight(Scanner scanner, FlightService flightService) {
        try {
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine();

            System.out.print("Enter destination: ");
            String destination = scanner.nextLine();

            System.out.print("Enter departure date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
            LocalDateTime dateTime = date.atStartOfDay();

            List<Flight> flights = flightService.searchFlights(destination, dateTime);

            if (flights.isEmpty()) {
                System.out.println("No available flights for that destination and date.");
                return;
            }

            System.out.println("Select a flight to book:");
            for (int i = 0; i < flights.size(); i++) {
                Flight f = flights.get(i);
                System.out.printf("%d) %s - %s at %s (Seats: %d)%n",
                        i + 1,
                        f.getFlightNumber(),
                        f.getDestination(),
                        f.getDepartureTime().format(DATE_TIME_FORMAT),
                        f.getAvailableSeats()
                );
            }

            System.out.print("Enter option number: ");
            int option = Integer.parseInt(scanner.nextLine());
            if (option < 1 || option > flights.size()) {
                System.out.println("Invalid option.");
                return;
            }

            Flight selected = flights.get(option - 1);

            System.out.print("Enter number of seats to book: ");
            int seats = Integer.parseInt(scanner.nextLine());

            Reservation reservation = flightService.bookFlight(customerName, selected, seats);
            System.out.println(" -- > Booking successful: " + reservation);
        } catch (Exception e) {
            System.out.println("Error while booking flight: " + e.getMessage());
        }
    }
//view reservation by name 
    private static void viewReservations(Scanner scanner, FlightService flightService) {
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        try {
            List<Reservation> reservations =
                    flightService.findReservationsByCustomer(customerName);

            if (reservations.isEmpty()) {
                System.out.println("No reservations found for " + customerName + ".");
            } else {
                System.out.println("Reservations for " + customerName + ":");
                for (Reservation r : reservations) {
                    System.out.println(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving reservations: " + e.getMessage());
        }
    }
}
