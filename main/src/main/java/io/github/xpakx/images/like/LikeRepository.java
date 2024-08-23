package io.github.xpakx.images.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndImageId(Long userId, Long imageId);

    Optional<Like> findByUserIdAndImageId(Long userId, Long imageId);

    long countByImageId(Long imageId);
}