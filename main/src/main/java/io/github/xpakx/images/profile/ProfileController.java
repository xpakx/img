package io.github.xpakx.images.profile;

import io.github.xpakx.images.profile.dto.ProfileData;
import io.github.xpakx.images.profile.dto.ProfileDetails;
import io.github.xpakx.images.profile.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @GetMapping("/profile/{username}")
    public ProfileDetails getUserProfile(@PathVariable String username, Principal principal) {
        return service.getUserProfile(username, principalToUsername(principal));
    }

    private String principalToUsername(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return null;
        }
        return principal.getName();
    }

    @PutMapping("/profile")
    public ProfileData updateImage(
            @Valid @RequestBody UpdateProfileRequest request,
            Principal principal
    ) {
        return service.updateProfile(request, principal.getName());
    }
}
