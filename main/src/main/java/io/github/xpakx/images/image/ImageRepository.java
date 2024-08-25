package io.github.xpakx.images.image;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findByUserUsername(String username, Pageable pageable);

    @Cacheable(value = "postCountCache", key = "#userId")
    long countByUserId(Long userId);
}