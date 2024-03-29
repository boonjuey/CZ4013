package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import model.Flight;
import model.Subscriber;
import dao.FlightDao;
import errors.DuplicateFlightIdException;
import errors.DuplicateSubscriberException;
import errors.InsufficientSeatsException;
import errors.InvalidFlightIdException;
import errors.NoFlightFoundException;
import errors.NoSubscriptionFoundException;

public class FlightController {
    private FlightDao flightDao;


    public FlightController(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    public List<Integer> getFlightIds(String source, String destination) throws NoFlightFoundException {
        List<Integer> flightIds = new ArrayList<>();
        for (Flight flight : flightDao.getFlights()) {
            if (flight.getSource().equals(source) && flight.getDestination().equals(destination)) {
                flightIds.add(flight.getFlightId());
            }
        }
        if (flightIds.isEmpty()) {
            throw new NoFlightFoundException();
        }
        return flightIds;
    }

    public Flight getFlightDetails(int flightId) throws InvalidFlightIdException {
        for (Flight flight : flightDao.getFlights()) {
            if (flight.getFlightId() == flightId) {
                return flight;
            }
        }
        throw new InvalidFlightIdException();
    }

    public void makeReservation(int flightId, int seatsToReserve)
            throws InvalidFlightIdException, InsufficientSeatsException {
        for (Flight flight : flightDao.getFlights()) {
            if (flight.getFlightId() == flightId) {
                if (flight.getAvailableSeats() < seatsToReserve) {
                    throw new InsufficientSeatsException();
                }
                flight.setAvailableSeats(flight.getAvailableSeats() - seatsToReserve);
                return;
            }
        }
        throw new InvalidFlightIdException();
    }

    public List<String> getDestinations() {
        Set<String> destinations = new HashSet<>();
        for (Flight flight : flightDao.getFlights()) {
            destinations.add(flight.getDestination());
        }
        return new ArrayList<>(destinations);
    }

    public void addFlight(Flight newFlight) throws DuplicateFlightIdException {
        for (Flight flight : flightDao.getFlights()) {
            if (flight.getFlightId() == newFlight.getFlightId()) {
                throw new DuplicateFlightIdException();
            }
        }
        flightDao.addFlight(newFlight);
    }
}
