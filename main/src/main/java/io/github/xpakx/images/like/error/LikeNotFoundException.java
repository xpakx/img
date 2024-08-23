package io.github.xpakx.images.like.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException (String message) {
        super(message);
    }
}
