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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ImageRepository imageRepository;

    public ProfileDetails getUserProfile(String username, String currentUser) {
        var user = profileRepository.findByUserUsername(username)
                .map(this::toProfileData)
                .orElseGet(() -> getDefaultProfile(username));

        long followers = followRepository.countByUserId(user.id());
        long following = followRepository.countByFollowerId(user.id());
        long posts = imageRepository.countByUserId(user.id());

        boolean followed = checkFollow(currentUser, user.id());

        return new ProfileDetails(
                user.username(),
                user.description(),
                posts,
                followers,
                following,
                followed,
                user.username().equals(currentUser)
        );
    }

    private boolean checkFollow(String currentUser, Long id) {
        if (currentUser == null) {
            return false;
        }
        var userId = userRepository.findByUsername(currentUser)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
        return followRepository.existsByUserIdAndFollowerId(id, userId);
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
