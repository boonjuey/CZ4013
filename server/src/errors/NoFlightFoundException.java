package errors;

public class NoFlightFoundException extends Exception {
    @Override
    public String getMessage() {
        return "No flights found";
    }
}
