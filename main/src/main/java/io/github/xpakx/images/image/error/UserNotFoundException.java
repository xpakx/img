package io.github.xpakx.images.image.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super(message);
    }

    public UserNotFoundException () {
        super("User not found");
    }
}
