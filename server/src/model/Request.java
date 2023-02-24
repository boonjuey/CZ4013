package model;

import java.net.InetAddress;
import java.util.Map;

public class Request {
    private Map<String, Object> requestBody;

    public Request(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    public int getRequestId() {
        return (int) requestBody.get("request_id");
    }

    public void setRequestId(int requestId) {
        requestBody.put("request_id", requestId);
    }

    public int getRequestType() {
        return (int) requestBody.get("request_type");
    }

    public void setRequestType(int requestType) {
        requestBody.put("request_type", requestType);
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    public InetAddress getClientAddress() {
        return (InetAddress) requestBody.get("client_address");
    }

    public void setClientAddress(InetAddress clientAddress) {
        requestBody.put("client_address", clientAddress);
    }

    public int getClientPort() {
        return (int) requestBody.get("client_port");
    }

    public void setClientPort(int clientPort) {
        requestBody.put("client_port", clientPort);
    }

    public String getSource() {
        return (String) requestBody.get("source");
    }

    public void setSource(String source) {
        requestBody.put("source", source);
    }

    public String getDestination() {
        return (String) requestBody.get("destination");
    }

    public void setDestination(String destination) {
        requestBody.put("destination", destination);
    }

    public int getFlightId() {
        return (int) requestBody.get("flight_id");
    }

    public void setFlightId(int flightId) {
        requestBody.put("flight_id", flightId);
    }

    public int getSeatsToReserve() {
        return (int) requestBody.get("seats_to_reserve");
    }

    public void setSeatsToReserve(int seatsToReserve) {
        requestBody.put("seats_to_reserve", seatsToReserve);
    }

    public int getDepartureTime() {
        return (int) requestBody.get("departure_time");
    }

    public void setDepartureTime(int departureTime) {
        requestBody.put("departure_time", departureTime);
    }

    public float getAirfare() {
        return (float) requestBody.get("airfare");
    }

    public void setAirfare(float airfare) {
        requestBody.put("airfare", airfare);
    }

    public int getAvailableSeats() {
        return (int) requestBody.get("available_seats");
    }

    public void setAvailableSeats(int availableSeats) {
        requestBody.put("available_seats", availableSeats);
    }
}