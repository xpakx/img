package io.github.xpakx.images.follow;

import io.github.xpakx.images.follow.dto.FollowRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService service;

    @PostMapping("/follows")
    public ResponseEntity<Object> follow(
            @Valid @RequestBody FollowRequest followRequest,
            Principal principal) {
        service.followUser(principal.getName(), followRequest.username());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follows/users/{username}")
    public ResponseEntity<Object> unfollow(
            @PathVariable String username,
            Principal principal) {
        service.unfollowUser(principal.getName(), username);
        return ResponseEntity.ok().build();
    }
}
