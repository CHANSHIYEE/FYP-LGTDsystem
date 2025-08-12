package shiyee_FYP.fullstack_backend.exception;

public class PortNotFoundException extends RuntimeException {
    public PortNotFoundException(String message) {
        super(message);
    }
}