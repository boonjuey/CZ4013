package errors;

public class DuplicateFlightIdException extends Exception {
    @Override
    public String getMessage() {
        return "Duplicate flight id";
    }
}