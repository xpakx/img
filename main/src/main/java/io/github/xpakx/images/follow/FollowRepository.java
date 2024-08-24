package io.github.xpakx.images.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserIdAndFollowerId(Long userId, Long followerId);

    Optional<Follow> findByUserIdAndFollowerId(Long userId, Long followerId);
}