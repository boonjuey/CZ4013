package controller;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.List;

import model.Flight;
import model.Request;
import utils.Marshaller;

public class ResponseController {
     @SuppressWarnings("unchecked")
     public DatagramPacket sendResponse(Request request, Object result, Exception e) {
          DatagramPacket response = null;
          if (e == null) {
               switch (request.getRequestType()) {
                    case 0:
                         response = prepareResponse(request, new HashMap<String, Object>() {
                              {
                                   put("result", (List<Integer>) result);
                              }
                         });
                         break;
                    case 1:
                         Flight flight = (Flight) result;
                         response = prepareResponse(request, new HashMap<String, Object>() {
                              {
                                   put("flightId", flight.getFlightId());
                                   put("source", flight.getSource());
                                   put("destination", flight.getDestination());
                                   put("departureTime", flight.getDepartureTime());
                                   put("airfare", flight.getAirfare());
                                   put("availableSeats", flight.getAvailableSeats());
                              }
                         });
                         break;
                    case 2:
                         response = prepareResponse(request, new HashMap<String, Object>() {
                              {
                                   put("result", "Reservation made successfully");
                              }
                         });
                         break;
                    case 3:
                         response = prepareResponse(request, new HashMap<String, Object>() {
                              {
                                   put("result", (List<String>) result);
                              }
                         });
                         break;
                    case 4:
                         response = prepareResponse(request, new HashMap<String, Object>() {
                              {
                                   put("result", "Flight added successfully");
                              }
                         });
                         break;
                    case 5:
                         // Monitor seats
                         break;
                    default:
                         response = prepareResponse(request, new HashMap<String, Object>() {
                              {
                                   put("error", "Invalid request");
                              }
                         });
                         break;
               }
          } else {
               response = prepareResponse(request, new HashMap<String, Object>() {
                    {
                         put("error", e.getMessage());
                    }
               });
          }
          return response;
     }

     public DatagramPacket prepareResponse(Request request, HashMap<String, Object> responseBody) {
          try {
               byte[] marshalledResponse = new Marshaller().marshal(responseBody);
               DatagramPacket response = new DatagramPacket(marshalledResponse, marshalledResponse.length,
                         request.getClientAddress(), request.getClientPort());
               return response;
          } catch (Exception e) {
               e.printStackTrace();
          }
          return null;
     }
}
