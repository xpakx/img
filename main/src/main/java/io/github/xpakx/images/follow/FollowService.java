package io.github.xpakx.images.follow;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.cache.annotation.CacheDecrement;
import io.github.xpakx.images.cache.annotation.CacheIncrement;
import io.github.xpakx.images.follow.error.AlreadyFollowedException;
import io.github.xpakx.images.follow.error.FollowNotFoundException;
import io.github.xpakx.images.follow.error.SelfFollowException;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowCountService followCountService;

    @CacheIncrement(value = "followCountCache", key = "#userUsername")
    @CacheIncrement(value = "followingCountCache", key = "#requesterUsername")
    public void followUser(String requesterUsername, String userUsername) {
        if (userUsername.equals(requesterUsername)) {
            throw new SelfFollowException("Cannot follow yourself.");
        }
        var requesterId = getUserId(requesterUsername);
        var userId = getUserId(userUsername);

        if (followRepository.existsByUserIdAndFollowerId(userId, requesterId)) {
            throw new AlreadyFollowedException("Already followed.");
        }

        Follow follow = new Follow();
        follow.setUser(userRepository.getReferenceById(userId));
        follow.setFollower(userRepository.getReferenceById(requesterId));

        followRepository.save(follow);
    }

    @CacheDecrement(value = "followCountCache", key = "#userUsername")
    @CacheDecrement(value = "followingCountCache", key = "#requesterUsername")
    public void unfollowUser(String requesterUsername, String userUsername) {
        var requesterId = getUserId(requesterUsername);
        var userId = getUserId(userUsername);
        Follow follow = followRepository.findByUserIdAndFollowerId(userId, requesterId)
                .orElseThrow(() -> new FollowNotFoundException("Follow not found."));
        followRepository.delete(follow);
    }

    private Long getUserId(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
    }

    public long getFollowersCount(String username) {
        var userId = getUserId(username);
        return followCountService.getFollowersCount(userId, username);
    }

    public long getFollowersCount(Long userId, String username) {
        return followCountService.getFollowersCount(userId, username);
    }

    public long getFollowingCount(String username) {
        var userId = getUserId(username);
        return followCountService.getFollowingCount(userId, username);
    }

    public long getFollowingCount(Long userId, String username) {
        return followCountService.getFollowingCount(userId, username);
    }
}
