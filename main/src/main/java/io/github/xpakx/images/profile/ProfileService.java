package io.github.xpakx.images.profile;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.follow.FollowRepository;
import io.github.xpakx.images.follow.FollowService;
import io.github.xpakx.images.image.ImageService;
import io.github.xpakx.images.image.error.UserNotFoundException;
import io.github.xpakx.images.profile.dto.ProfileData;
import io.github.xpakx.images.profile.dto.ProfileDetails;
import io.github.xpakx.images.profile.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FollowService followService;
    private final ImageService imageService;

    public ProfileDetails getUserProfile(String username, String currentUser) {
        var user = profileRepository.findByUserUsername(username)
                .map(this::toProfileData)
                .orElseGet(() -> getDefaultProfile(username));

        long followers = followService.getFollowersCount(user.id(), username);
        long following = followService.getFollowingCount(user.id(), username);
        long posts = imageService.getPostCount(user.id(), username);

        boolean followed = checkFollow(currentUser, user.id());

        return new ProfileDetails(
                user.username(),
                user.description(),
                posts,
                followers,
                following,
                followed,
                user.username().equals(currentUser),
                user.avatarUrl()
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
                profile.getDescription(),
                profile.getUser().getAvatarUrl()
        );
    }

    private ProfileData toProfileData(Profile profile, String username, Long userId) {
        return new ProfileData(
                userId,
                username,
                profile.getDescription(),
                profile.getUser().getAvatarUrl()
        );
    }

    private ProfileData toProfileData(User user) {
        return new ProfileData(
                user.getId(),
                user.getUsername(),
                "",
                "avatars/default.jpg"
        );
    }

    public ProfileData updateProfile(UpdateProfileRequest request, String username) {
        var profile = profileRepository.findByUserUsername(username)
                .orElseGet(() -> createProfile(username));
        profile.setDescription(request.description());
        var result = profileRepository.save(profile);
        return toProfileData(result, username, profile.getUser().getId());
    }

    private Profile createProfile(String username) {
        return userRepository.findByUsername(username)
                .map(this::toProfile)
                .orElseThrow(UserNotFoundException::new);
    }

    private Profile toProfile(User user) {
        var profile =  new Profile();
        profile.setUser(userRepository.getReferenceById(user.getId()));
        profile.setDescription("");
        return profile;
    }
}
