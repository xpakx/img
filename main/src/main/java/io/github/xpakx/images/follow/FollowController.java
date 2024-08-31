package io.github.xpakx.images.follow;

import io.github.xpakx.images.follow.dto.FollowRequest;
import io.github.xpakx.images.follow.dto.UserFollows;
import io.github.xpakx.images.like.dto.ImageLikes;
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

    @GetMapping("/user/{username}/follows")
    public UserFollows getFollowCount(@PathVariable String username) {
        return new UserFollows(
                service.getFollowersCount(username),
                service.getFollowingCount(username)
        );
    }
}
