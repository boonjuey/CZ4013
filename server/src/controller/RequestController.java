package controller;

import java.net.DatagramPacket;
import java.util.Map;

import dao.FlightDao;
import errors.DuplicateFlightIdException;
import errors.DuplicateSubscriberException;
import errors.InsufficientSeatsException;
import errors.InvalidFlightIdException;
import errors.NoFlightFoundException;
import errors.NoSubscriptionFoundException;
import model.Flight;
import model.Request;
import model.Subscriber;
import utils.Marshaller;

public class RequestController {
    private FlightController flightController;
    private CallbackController callbackController;

    public RequestController(FlightDao flightDao, CallbackController callbackController) {
        flightController = new FlightController(flightDao);
        this.callbackController = callbackController;
    }

    public Request getRequest(DatagramPacket packet) {
        Map<String, Object> requestBody = new Marshaller().unmarshal(packet.getData());
        Request request = new Request(requestBody);
        request.setClientAddress(packet.getAddress());
        request.setClientPort(packet.getPort());
        return request;
    }

    public Object processRequest(Request request) throws DuplicateFlightIdException,
            InsufficientSeatsException, InvalidFlightIdException, NoFlightFoundException, DuplicateSubscriberException, NoSubscriptionFoundException {
        switch (request.getRequestType()) {
            case 0:
                return flightController.getFlightIds(request.getSource(), request.getDestination());
            case 1:
                return flightController.getFlightDetails(request.getFlightId());
            case 2:
                flightController.makeReservation(request.getFlightId(), request.getSeatsToReserve());
                return null;
            case 3:
                return flightController.getDestinations();
            case 4:
                flightController
                        .addFlight(new Flight(request.getFlightId(), request.getSource(), request.getDestination(),
                                request.getDepartureTime(), request.getAirfare(), request.getAvailableSeats()));
                return null;
            case 5:
                // Function to run callback
                // callbackController.runCallback...
                return callbackController
                        .addSubscriber(request.getFlightId(), new Subscriber(request.getClientAddress(), 
                            request.getClientPort(), request.getDuration()));
            
            case 6: 
                return callbackController
                        .removeSubscriber(request.getFlightId(), new Subscriber(request.getClientAddress(), 
                            request.getClientPort(), request.getDuration()));
                
                

            default:
                return null;
        }
    }
}
