package shiyee_FYP.fullstack_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'",
                resourceName, fieldName, fieldValue));
    }

    public static class DataImportException extends RuntimeException {
        public DataImportException(String message) {
            super(message);
        }

        public DataImportException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class PortNotFoundException extends RuntimeException {
        public PortNotFoundException(Long portId) {
            super("港口ID " + portId + " 不存在或缺少坐标数据");
        }
    }
}