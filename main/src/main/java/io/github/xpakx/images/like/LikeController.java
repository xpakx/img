package io.github.xpakx.images.like;

import io.github.xpakx.images.image.dto.ImageData;
import io.github.xpakx.images.like.dto.ImageLikes;
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
        service.likeImage(principal.getName(), likeRequest.imageId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/likes/images/{imageId}")
    public ResponseEntity<Object> unlike(
            @PathVariable String imageId,
            Principal principal) {
        service.unlikeImage(principal.getName(), imageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/{id}/likes")
    public ImageLikes getImageById(@PathVariable String id) {
        return service.getLikeCount(id);
    }
}
