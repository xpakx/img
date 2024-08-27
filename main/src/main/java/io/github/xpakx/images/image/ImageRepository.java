package io.github.xpakx.images.image;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findByUserUsername(String username, Pageable pageable);

    @Cacheable(value = "postCountCache", key = "#userId")
    long countByUserId(Long userId);

    @Query("""
    SELECT image
    FROM Image i
    JOIN Like l ON l.image.id = i.id
    WHERE l.user.id = :userId
    """)
   Page<Image> getLiked(Long userId, Pageable pageable);
}