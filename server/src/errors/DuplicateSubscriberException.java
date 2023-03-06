package errors;

public class DuplicateSubscriberException extends Exception{
    @Override
    public String getMessage() {
        return "You have already subscribed to this flight";
    }
}
