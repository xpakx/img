package io.github.xpakx.images.like.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageOwnerException extends RuntimeException {
    public ImageOwnerException (String message) {
        super(message);
    }
}
