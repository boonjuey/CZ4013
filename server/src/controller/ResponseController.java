package controller;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.List;

import model.Flight;
import model.Request;
import model.Response;
import utils.Marshaller;

public class ResponseController {
     @SuppressWarnings("unchecked")
     public DatagramPacket prepareResponse(Request request, Object result, Exception e) {
          Response response = null;
          if (e == null) {
               switch (request.getRequestType()) {
                    case 0:
                         response = new Response(new HashMap<String, Object>() {
                              {
                                   put("result", (List<Flight>) result);
                              }
                         });
                         break;
                    case 1:
                         Flight flight = (Flight) result;
                         response = new Response(new HashMap<String, Object>() {
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
                         response = new Response(new HashMap<String, Object>() {
                              {
                                   put("result", "Reservation made successfully");
                              }
                         });
                         break;
                    case 3:
                         response = new Response(new HashMap<String, Object>() {
                              {
                                   put("result", (List<String>) result);
                              }
                         });
                         break;
                    case 4:
                         response = new Response(new HashMap<String, Object>() {
                              {
                                   put("result", "Flight added successfully");
                              }
                         });
                         break;
                    case 5:
                         // Monitor seats
                         break;
                    default:
                         response = new Response(new HashMap<String, Object>() {
                              {
                                   put("error", "Invalid request");
                              }
                         });
                         break;
               }
          } else {
               response = new Response(new HashMap<String, Object>() {
                    {
                         put("error", e.getMessage());
                    }
               });
          }

          try {
               byte[] marshalledResponse = new Marshaller().marshal(response.getResponseBody());
               DatagramPacket responsePacket = new DatagramPacket(marshalledResponse, marshalledResponse.length,
                         request.getClientAddress(), request.getClientPort());
               return responsePacket;
          } catch (Exception ex) {
               ex.printStackTrace();
          }
          return null;
     }
}
