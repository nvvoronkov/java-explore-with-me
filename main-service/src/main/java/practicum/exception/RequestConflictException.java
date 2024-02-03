package practicum.exception;

public class RequestConflictException extends RuntimeException {
    public RequestConflictException(String message) {
        super(message);
    }
}
