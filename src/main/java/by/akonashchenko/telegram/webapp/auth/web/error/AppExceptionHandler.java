package by.akonashchenko.telegram.webapp.auth.web.error;

import by.akonashchenko.telegram.webapp.auth.domain.model.exception.ValidationException;
import by.akonashchenko.telegram.webapp.auth.web.jte.model.Error;
import by.akonashchenko.telegram.webapp.auth.web.service.JteResponseWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    private final JteResponseWriter responseWriter;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIIllegalArgumentException(IllegalArgumentException ex) {
        Error error = Error.builder()
                .message(ex.getMessage())
                .build();
        return renderError(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        Error error = Error.builder()
                .message(ex.getMessage())
                .build();
        return renderError(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        Error error = Error.builder()
                .message(ex.getMessage())
                .build();
        return renderForbidden(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        Error error = Error.builder()
                .message(ex.getMessage())
                .build();
        return renderError(HttpStatus.INTERNAL_SERVER_ERROR, error);
    }

    private ResponseEntity<String> renderError(HttpStatus status, Object model) {
        return responseWriter.writeHtml("error.jte", model, status);
    }

    private ResponseEntity<String> renderForbidden(Object model) {
        return responseWriter.writeHtml("forbidden.jte", model, FORBIDDEN);
    }
}
