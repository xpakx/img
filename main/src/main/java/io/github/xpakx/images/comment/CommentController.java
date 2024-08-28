package io.github.xpakx.images.comment;

import io.github.xpakx.images.comment.dto.CommentData;
import io.github.xpakx.images.comment.dto.CommentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @PostMapping("/image/{id}/comment")
    public ResponseEntity<CommentData> postComment(
            @Valid @RequestBody CommentRequest request,
            @PathVariable String id,
            Principal principal
    ) {
        return new ResponseEntity<>(
                service.addComment(request, id, principal.getName()),
                HttpStatus.CREATED
        );
    }
}
