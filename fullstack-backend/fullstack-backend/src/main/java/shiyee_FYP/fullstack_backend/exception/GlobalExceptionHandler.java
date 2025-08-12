package shiyee_FYP.fullstack_backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        response.put("details", ex.getClass().getSimpleName());

        // 记录完整堆栈跟踪到日志
        log.error("全局异常捕获", ex);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGlobalException(Exception ex) {
//        return new ResponseEntity<>("Global error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

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
