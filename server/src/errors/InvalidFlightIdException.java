package errors;

public class InvalidFlightIdException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid flight id";
    }
}