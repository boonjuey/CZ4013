package errors;

public class NoSubscriptionFoundException extends Exception{
    @Override
    public String getMessage() {
        return "You do not have any subscriptions for this flight";
    }
}
