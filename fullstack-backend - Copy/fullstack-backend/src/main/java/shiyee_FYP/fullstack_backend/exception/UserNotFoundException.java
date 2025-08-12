package shiyee_FYP.fullstack_backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not found the user with id " + id);;
    }

    public UserNotFoundException(String username) {
        super("Could not found the user with username " +username);
    }
}
