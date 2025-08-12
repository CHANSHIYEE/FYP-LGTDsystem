package shiyee_FYP.fullstack_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Global error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class NoSuitablePortException extends RuntimeException {
        public NoSuitablePortException(String message) {
            super(message);
        }
    }

    public class NoShippingRouteException extends RuntimeException {
        public NoShippingRouteException(String message) {
            super(message);
        }
    }

    public static class RouteCalculationException extends RuntimeException {
        public RouteCalculationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
