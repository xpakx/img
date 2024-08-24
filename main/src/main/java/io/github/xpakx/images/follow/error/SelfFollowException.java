package io.github.xpakx.images.follow.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SelfFollowException extends RuntimeException {
    public SelfFollowException (String message) {
        super(message);
    }
}
