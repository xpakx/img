package io.github.xpakx.images.follow;

import io.github.xpakx.images.account.User;
import io.github.xpakx.images.account.UserRepository;
import io.github.xpakx.images.image.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void followUser(String userUsername, String requesterUsername) {
        if (userUsername.equals(requesterUsername)) {
            throw new RuntimeException("Cannot follow yourself.");
        }
        var requesterId = userRepository.findByUsername(requesterUsername)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
        var userId = userRepository.findByUsername(userUsername)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);


        if (followRepository.existsByUserIdAndFollowerId(userId, requesterId)) {
            throw new RuntimeException("Already followed.");
        }

        Follow follow = new Follow();
        follow.setUser(userRepository.getReferenceById(userId));
        follow.setFollower(userRepository.getReferenceById(requesterId));

        followRepository.save(follow);
    }
}
