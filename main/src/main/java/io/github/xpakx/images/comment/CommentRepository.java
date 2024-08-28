package io.github.xpakx.images.comment;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Cacheable(value = "commentCountCache", key = "#imageId")
    long countByImageId(Long imageId);
}