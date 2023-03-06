package errors;

public class FlightNotFoundException extends Exception{

    @Override
    public String getMessage() {
        return "This flight does not exist";
    }
}
