package errors;

public class DuplicateRequestException extends Exception {
    @Override
    public String getMessage() {
        return "Duplicate request";
    }
}
