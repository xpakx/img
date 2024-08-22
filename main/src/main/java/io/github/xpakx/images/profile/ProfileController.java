package io.github.xpakx.images.profile;

import io.github.xpakx.images.profile.dto.ProfileData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @GetMapping("/profile/{username}")
    public ProfileData getUserProfile(@PathVariable String username) {
        return service.getUserProfile(username);
    }
}