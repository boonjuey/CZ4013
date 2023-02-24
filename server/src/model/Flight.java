package model;

public class Flight {
    private int flightId;
    private String source;
    private String destination;
    private int departureTime;
    private float airfare;
    private int availableSeats;

    public Flight(int flightId, String source, String destination, int departureTime, float airfare,
            int availableSeats) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.airfare = airfare;
        this.availableSeats = availableSeats;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public float getAirfare() {
        return airfare;
    }

    public void setAirfare(float airfare) {
        this.airfare = airfare;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        int before = this.availableSeats;
        this.availableSeats = availableSeats;
        System.out.println("Available seats for flight " + flightId + " changed from " + before + " to " + availableSeats);
    }

    public String toString() {
        return "Flight ID: " + flightId + ", Source: " + source + ", Destination: " + destination
                + ", Departure Time: " + departureTime + ", Airfare: " + airfare + ", Available Seats: "
                + availableSeats;
    }
}