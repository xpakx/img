package io.github.xpakx.images.follow;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.follow.dto.UserFollows;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    public void followUser(String requesterUsername, String userUsername) {
        if (userUsername.equals(requesterUsername)) {
            throw new RuntimeException("Cannot follow yourself.");
        }
        var requesterId = getUserId(requesterUsername);
        var userId = getUserId(userUsername);

        if (followRepository.existsByUserIdAndFollowerId(userId, requesterId)) {
            throw new RuntimeException("Already followed.");
        }

        Follow follow = new Follow();
        follow.setUser(userRepository.getReferenceById(userId));
        follow.setFollower(userRepository.getReferenceById(requesterId));

        followRepository.save(follow);
        updateFollowCountCache(userId, 1);
        updateFollowingCountCache(requesterId, 1);
    }

    public void unfollowUser(String requesterUsername, String userUsername) {
        var requesterId = getUserId(requesterUsername);
        var userId = getUserId(userUsername);
        Follow follow = followRepository.findByUserIdAndFollowerId(userId, requesterId)
                .orElseThrow(() -> new RuntimeException("Follow not found."));
        followRepository.delete(follow);
        updateFollowCountCache(userId, -1);
        updateFollowingCountCache(requesterId, -1);
    }

    private Long getUserId(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserFollows getFollowCount(String username) {
        var userId = getUserId(username);
        var followers = followRepository.countByUserId(userId);
        var following = followRepository.countByFollowerId(userId);
        return new UserFollows(followers, following);
    }

    private void updateFollowCountCache(Long userId, int delta) {
        var cache = cacheManager.getCache("followCountCache");
        if (cache != null) {
            Long currentLikeCount = cache.get(userId, Long.class);
            cache.put(
                    userId,
                    Objects.requireNonNullElseGet(
                            currentLikeCount,
                            () -> followRepository.countByUserId(userId)
                    ) + delta
            );
        }
    }

    private void updateFollowingCountCache(Long userId, int delta) {
        var cache = cacheManager.getCache("followingCountCache");
        if (cache != null) {
            Long currentLikeCount = cache.get(userId, Long.class);
            cache.put(
                    userId,
                    Objects.requireNonNullElseGet(
                            currentLikeCount,
                            () -> followRepository.countByFollowerId(userId)
                    ) + delta
            );
        }
    }
}
