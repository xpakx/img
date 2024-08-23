package io.github.xpakx.images.like;

import io.github.xpakx.images.account.dto.RegistrationRequest;
import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.like.dto.LikeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService service;

    @PostMapping("/likes")
    public ResponseEntity<Object> addLike(
            @Valid @RequestBody LikeRequest likeRequest,
            Principal principal) {
        service.likeImage(likeRequest.imageId(), principal.getName());
        return ResponseEntity.ok().build();
    }
}
