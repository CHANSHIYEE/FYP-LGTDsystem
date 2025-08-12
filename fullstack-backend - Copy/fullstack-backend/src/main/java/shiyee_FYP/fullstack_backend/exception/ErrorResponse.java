package shiyee_FYP.fullstack_backend.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        String message

){

}