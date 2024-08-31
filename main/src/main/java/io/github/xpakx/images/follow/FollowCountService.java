package io.github.xpakx.images.follow;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowCountService {
    private final FollowRepository followRepository;

    @Cacheable(value = "followCountCache", key = "#username")
    public long getFollowersCount(Long userId, String username) {
        return followRepository.countByUserId(userId);
    }

    @Cacheable(value = "followingCountCache", key = "#username")
    public long getFollowingCount(Long userId, String username) {
        return followRepository.countByFollowerId(userId);
    }
}
