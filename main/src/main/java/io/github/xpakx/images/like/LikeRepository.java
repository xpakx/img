package io.github.xpakx.images.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndImageId(Long userId, Long imageId);
}