package controller;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.List;

import model.Flight;
import model.Request;
import model.Response;
import model.Subscriber;
import model.Subscription;
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
                                   put("requestType", "0");
                                   put("result", (List<Flight>) result);
                              }
                         });
                         break;
                    case 1:
                         Flight flight = (Flight) result;
                         response = new Response(new HashMap<String, Object>() {
                              {    
                                   put("requestType", "1");
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
                                   put("requestType", "2");
                                   put("result", "Reservation made successfully");
                              }
                         });
                         break;
                    case 3:
                         response = new Response(new HashMap<String, Object>() {
                              {    
                                   put("requestType", "3");
                                   put("result", (List<String>) result);
                              }
                         });
                         break;
                    case 4:
                         response = new Response(new HashMap<String, Object>() {
                              {    
                                   put("requestType", "4");
                                   put("result", "Flight added successfully");
                              }
                         });
                         break;
                    case 5:
                         Subscription subscription = (Subscription) result;
                         System.out.println("Case5 : Flight id" + subscription.getFlightId());
                         // Monitor seats
                         response = new Response(new HashMap<String, Object>() {
                              {    
                                   put("requestType", "5");
                                   put("flightId", subscription.getFlightId());
                                   put("result", "Flight subscribed successfully");
                                   put("duration", subscription.getDuration());
                              }
                         });
                         break;
                    
                    case 6: 
                         Integer flightId = (Integer) result;
                         System.out.println("Flight id" + flightId);
                         response = new Response(new HashMap<String, Object>() {
                              {    
                                   put("requestType", "6");
                                   put("flightId", flightId);
                                   put("result", "Flight unsubscribed successfully");
                              }
                         });
                         break;
                        

                    default:
                         response = new Response(new HashMap<String, Object>() {
                              {    
                                   put("requestType", "error");
                                   put("error", "Invalid request");
                              }
                         });
                         break;
               }
          } else {
               response = new Response(new HashMap<String, Object>() {
                    {    
                         put("requestType", "error");
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
