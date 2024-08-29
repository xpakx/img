package io.github.xpakx.images.priv;

import io.github.xpakx.images.priv.dto.MessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/messages/{id}")
    public void deleteMessage(@PathVariable Long id, Principal principal) {
        service.deleteMessage(id, principal.getName());
    }
}
