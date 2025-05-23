package by.akonashchenko.telegram.webapp.auth.domain.model.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
