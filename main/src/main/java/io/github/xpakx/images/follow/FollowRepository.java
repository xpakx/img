package io.github.xpakx.images.follow;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserIdAndFollowerId(Long userId, Long followerId);

    Optional<Follow> findByUserIdAndFollowerId(Long userId, Long followerId);

    @Cacheable(value = "followCountCache", key = "#userId")
    long countByUserId(Long userId);

    @Cacheable(value = "followingCountCache", key = "#followerId")
    long countByFollowerId(Long followerId);
}