package io.github.xpakx.images.like.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LikeExistsException extends RuntimeException {
    public LikeExistsException (String message) {
        super(message);
    }
}
