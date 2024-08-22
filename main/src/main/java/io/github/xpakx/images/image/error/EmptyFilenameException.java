package io.github.xpakx.images.image.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyFilenameException extends  RuntimeException {
    public EmptyFilenameException (String message) {
        super(message);
    }
}
