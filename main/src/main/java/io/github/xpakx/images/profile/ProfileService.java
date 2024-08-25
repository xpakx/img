package io.github.xpakx.images.profile;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.follow.FollowRepository;
import io.github.xpakx.images.image.ImageRepository;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.profile.dto.ProfileData;
import io.github.xpakx.images.profile.dto.ProfileDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ImageRepository imageRepository;

    public ProfileDetails getUserProfile(String username) {
        var user = profileRepository.findByUserUsername(username)
                .map(this::toProfileData)
                .orElseGet(() -> getDefaultProfile(username));

        long followers = followRepository.countByUserId(user.id());
        long following = followRepository.countByFollowerId(user.id());
        long posts = imageRepository.countByUserId(user.id());

        return new ProfileDetails(
                user.username(),
                user.description(),
                posts,
                followers,
                following
        );
    }

    private ProfileData getDefaultProfile(String username) {
        return userRepository.findByUsername(username)
                .map(this::toProfileData)
                .orElseThrow(UserNotFoundException::new);
    }

    private ProfileData toProfileData(Profile profile) {
        return new ProfileData(
                profile.getUser().getId(),
                profile.getUser().getUsername(),
                profile.getDescription()
        );
    }

    private ProfileData toProfileData(User user) {
        return new ProfileData(
                user.getId(),
                user.getUsername(),
                ""
        );
    }
}
