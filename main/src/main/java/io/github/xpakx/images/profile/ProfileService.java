package io.github.xpakx.images.profile;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.profile.dto.ProfileData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileData getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .map(this::toProfileData)
                .orElseThrow(UserNotFoundException::new);
    }

    private ProfileData toProfileData(User user) {
        return new ProfileData(user.getUsername());
    }

}
