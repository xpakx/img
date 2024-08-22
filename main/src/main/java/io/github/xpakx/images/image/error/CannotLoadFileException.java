package io.github.xpakx.images.image.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotLoadFileException extends RuntimeException {
    public CannotLoadFileException (String message) {
        super(message);
    }
}
