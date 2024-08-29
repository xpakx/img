package io.github.xpakx.images.priv;

import io.github.xpakx.images.priv.dto.MessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PrivateMessageController {
    private final PrivateMessageService service;

    @PostMapping("/messages")
    public ResponseEntity<PrivateMessage> sendMessage(
            @Valid @RequestBody MessageRequest request,
            Principal principal) {
        return new ResponseEntity<>(
                service.sendMessage(request, principal.getName()),
                HttpStatus.CREATED
        );
    }
}
