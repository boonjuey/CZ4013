package errors;

public class InsufficientSeatsException extends Exception {
    @Override
    public String getMessage() {
        return "Insufficient seats";
    }
}
