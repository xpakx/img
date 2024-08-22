package io.github.xpakx.images.image.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAnOwnerException extends RuntimeException {
    public NotAnOwnerException (String message) {
        super(message);
    }
}
